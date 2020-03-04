# ✨ bongad

## 🚀 Lotto Demo App

### 👋1. data폴더

경로 : com.bong.fragment/data
>Apiservice,
>Lotto,
>Frequency

기능: Apiservice 인터페이스와 Apiservice 인터페이스로 얻을 데이터 목록 분류(Lotto, Frequency)

### 📝2. network폴더

경로 : com.bong.fragment/network
>RetrofitMaker

기능 : REST API - 서버와 클라이언트간 HTTP 통신을 위한 인터페이스

*SingleTone pattern*
어플리케이션 시작 시 최초 메모리를 한번만 할당 이후 메모리에 인스턴스를 만들어 사용하는 디자인패턴.

### ⭐️3. room

경로 : com.bong.fragment/room

>AppDatabase,
>LottoDao

-기능-

AppDatabase : 데이터베이스 Lotto와 Frequency 테이블 생성. 현재 어플에선 Lotto테이블만 사용

LottoDao : Data Access Object생성. 데이터베이스를 통해 수행 할 작업을 정의한 클래스.

### 🤝4. ui

경로 : com.bong.fragment/ui

1. History
>HisstoryFragment

1회부터 50회까지의 당첨 번호를 알려준다.
리사이클러 뷰 리턴

     View rootView = inflater.inflate(R.layout.item_list, container, false);
HomeFragment에서 Bundle로 넘긴 data 받기.

     private void initDataset(){
        Bundle extra = this.getArguments();
        ArrayList<String>list = extra.getStringArrayList("list");        
        ArrayList<String>str = extra.getStringArrayList("str");  
        data = new ArrayList<>();     
     }

>LottoAdapter : HistoryFragment에 연결할 어뎁터
리사이클러뷰에 표시할 ViewHolder에 fragment_history 연결

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.fragment_history, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

holder에 LottoNum에서 받은 String 객체를 position번째에 넣어준다.

    @Override
    public void onBindViewHolder(@NonNull LottoAdapter.Holder holder, int position) {
        holder.LottoNum.setText(m_Datas.get(position).LottoNum);
        holder.WinNum.setText(m_Datas.get(position).WinNum);
    }
m_Datas의 개수만큼을 리턴한다 여기서 m_Datas는 \' private ArrayList<LottoNum> m_Datas; \' 을 말한다.

    @Override
    public int getItemCount() {
        return m_Datas.size();
    }
Holder라는 클래스 생성하고 hitoryfragment의 Textview에 연결.

    public class Holder extends RecyclerView.ViewHolder{
        public TextView LottoNum;
        public TextView WinNum;
        public Holder(@NonNull View itemView) {
            super(itemView);
            LottoNum = (TextView)itemView.findViewById(R.id.tv_count);
            WinNum = (TextView)itemView.findViewById(R.id.tv_message);
        }
    }
>LottoNum : getter와 setter를 생성하고 String형식으로 화면에 표시될 문자열을 초기화 시킴.

2. Splah
>SplashFargment
기능

view생성시 fragment_splash 레이아웃이 연결, 

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_splash, container, false);
    }
disaposable은 Compositedisaposable로 이클래스를 사용하면 생성된 모든 Observable을 안드로이드 라이프사이클에 맞춰서 한번에 메모리 해제가능.

    disposables = new CompositeDisposable();

dao는 LottoDao 초기화시킨것이며 이 프레그먼트에서 데이터를 받아오게 한다.

    dao = AppDatabase.getDatabase(getActivity()).getLottoDao();

getLottoAndSave();는 homefragment로 보내줄 ArrayList들을 list, str, pre를 생성하고,
Apiservice로 1 ~ 50번까지 복수개 통신을 한 후, room에 저장이 끝나면
send(list, str, pre)를 HomeFragment로 보내주고 HomeFragment로 화면을 바꾼다.

    public void getLottoAndSave(){
        Apiservice apiService = new RetrofitMaker().createService(getActivity(), Apiservice.class);

        ArrayList<String>list = new ArrayList<>();
        ArrayList<String> str = new ArrayList<>();
        ArrayList<Integer>pre = new ArrayList<>();

        // 복수개 통신
        ArrayList<Single<Lotto>> temp = new ArrayList<Single<Lotto>>();
        for(int i = 1; i <= 50; i++){
            temp.add(
                    apiService.getCommentRx(i)
                            .map(lotto -> {

                                ArrayList<Integer>num = new ArrayList<>();
                                ArrayList<Integer>win = new ArrayList<>();
                                num.add(lotto.drwNo);
                                win.add(lotto.drwtNo1);
                                win.add(lotto.drwtNo2);
                                win.add(lotto.drwtNo3);
                                win.add(lotto.drwtNo4);
                                win.add(lotto.drwtNo5);
                                win.add(lotto.drwtNo6);
                                win.add(lotto.bnusNo);

                                list.add("" + num.get(0));
                                for(int j = 0; j < win.size(); j++){
                                    pre.add(win.get(j));
                                }
                                str.add(convertIntoString(win));

                                return lotto;
                            })
            );
        }

        final boolean add = disposables.add(Single.concat(temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Lotto>(){
                    @Override
                    public void onNext(Lotto lotto) {}
                    @Override
                    public void onError(Throwable t) { t.printStackTrace(); }
                    @Override
                    public void onComplete() {

                        send(list, str, pre);
                        ((MainActivity)getActivity()).changeFragment(MainActivity.Type.home, homeFragment);
                    }
                }));
    }
3. TrendFragment

1번부터 45번까지 중 제일 자주 출현하는 숫자의 횟수를 보여준다.

>TrendFragment
HomeFragment에서 보낸 데이터를 initDataset()에서 받고 HashMap으로 String, Integer 타입을 받고,
cnt를 Integer형식으로 초기화 시킨후 pre에 데이터를 받은 후 int vaseVal에 cnt가 null이면 vaseVal값을 넣고, false일시 0을 넣는다.
cntMap에 key값은 1~45번까지의 String형식으로 넣어주고 같은 번호 출현시 baseVal + 1해준 값을 넣어준다.

    private void initDataset(){
        Bundle extra = this.getArguments();
        ArrayList<Integer>pre = extra.getIntegerArrayList("pre");
        Map<String, Integer>cntMap = new HashMap<>();

        for(int i = 0; i < pre.size(); i++) {
            Integer cnt = (Integer) cntMap.get(pre.get(i).toString());

            int baseVal = (cnt != null) ? cnt.intValue() : 0;
            cntMap.put(pre.get(i).toString(), baseVal + 1);
        }
        convert(cntMap);
    }  
value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬

    private void convert(Map<String, Integer>cntMap){
        
        List<Map.Entry<String, Integer>>list = new LinkedList<>(cntMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int comparision = (o1.getValue() - o2.getValue()) *  -1;

                return comparision == 0? o1.getKey().compareTo(o2.getKey()) : comparision;
            }
        });
순서 유지를 위해 LinkedHashMap을 사용

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for(Iterator<Map.Entry<String, Integer>> iter = list.iterator(); iter.hasNext();){
            Map.Entry<String, Integer> entry = iter.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        addlist(sortedMap);
    }

ArrayList<LottoNum>data를 초기화 시키고 1번부터 45번까지를 key값으로 넣고 출현 횟수를 value값에 넣어주고 출력

    private void addlist(Map<String, Integer> sortedMap){
        data = new ArrayList<>();
        Iterator<String> iter = sortedMap.keySet().iterator();
        while (iter.hasNext()) {
            try{
                String key = iter.next();
                int value = sortedMap.get(key);
                String val = Integer.toString(value);
                data.add(new LottoNum("No."+ key, val + " times"))  ;
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

나머지 기능들은 History패키지에 있는 것들과 같다.
>LottoAdapter,
>LottoNum

4. 나머지
>AppApplication : Stetho 사용 목적(크롬에서 DB데이터를 눈으로 직접 확인 가능)

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);
    }
>HomeFragment
fragment_main을 view로 리턴 

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }
onViewCreated에 버튼 리스너들 구현하고, generateBtn을 클릭시 - EditText인 tv_result의 텍스트 값이 변하는것에 따라 버튼과 레이아웃 구성합니다.
입력되는 EditText에 변화가 있으면 v_result의 할당된 구역과 레이아웃이 사라지고, generateBtn과 result_bt은 tv_result의 값이 0이 아니면 활성화되고 0일시에 비활성화 된다.

    generateBtn.setOnClickListener(v -> {
      tv_result.addTextChangedListener(new TextWatcher() {
        //입력전
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        //입력되는 EditText에 변화가 있을때
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          layout.setVisibility(View.GONE);
        }
        //입력후
        @Override
        public void afterTextChanged(Editable s) {
          String text = s.toString();
            if (text.length() != 0) {
              result_bt.setEnabled(true);
              generateBtn.setEnabled(true);//버튼 활성화
              String drwNo = tv_result.getText().toString();
              int num = Integer.parseInt(drwNo);
              if (num > 0 && num < 51) {
                result_bt.setOnClickListener(v -> {
                  callAPIs();
                });
              } else {
              //결과보기 버튼 클릭 시 다이얼로그 생성(당첨번호와 회차번호 비교 후 등수 출력)
                result_bt.setOnClickListener(v -> {
                  if (flag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.notification);
                    builder.setMessage(R.string.possible);
                    builder.setPositiveButton(R.string.check, new                                             DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        flag = false;
                        Toast.makeText(getActivity(), R.string.check, Toast.LENGTH_LONG);
                      }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                      @Override
                      public void onCancel(DialogInterface dialog) {
                        flag = true;
                      }
                    });
                    builder.show();
                    flag = false;
                 }
               });
             }
           } else {
               result_bt.setEnabled(false);//버튼 비활성
               generateBtn.setEnabled(false);
           }
         }
       });
     })
     
>MainActivity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment splashFragment;
        splashFragment = new SplashFragment();
        Toolbar(0);
        changeFragment(Type.splash, splashFragment);
    }


    public void Toolbar(int num){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch (num){
            case 0:
                getSupportActionBar().hide();
                break;
            case 1:
                getSupportActionBar().show();
                getSupportActionBar().setTitle(R.string.title_main);
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.title_history);
                break;
            case 3:
                getSupportActionBar().setTitle(R.string.title_trend);
                break;
        }
    }

enum을 사용해서 코드가 단순해지며 가독성이 좋습니다. 인스턴스 생성과 상속을 방지합니다.

    public enum Type {
        splash, home, trend, history
    }

changeFragment메소드 - 이프로젝트에서 엑티비티는 MainActivity 하나 이기 때문에 모든 프래그먼트들은 교체 해주기 위해 만듭니다.
Aactivity 또는 Fragmentr간의 상호작용을 위해 이어주는 역할을 하고 생성, 대체, 삭제를 하기 원활하게 하기 위해서만만들어줘야 하는 것들에 대해 만들어 놓은 메소드 입니다.

1.FragmentManager fragmentManager를 만들어줍니다. -> 엑티비티나 프래그먼트 간의 상호작용하게 해줍니다.

2.FragmentTransaction transaction을 만들어줍니다. -> 교체, 생성, 삭제 또는 Backstack저장하는 작업들을 할 수있게 해줍니다.

3.transaction = fragmentManager.beginTransaction(); - beginTransaction();을 호출해주고 이후에 교체, 생성, 삭제 등이 가능.

4.그리고 항상 마지막으로 제일 중요한 transaction.commit();을 해줘야 transaction작업을 정상적으로 수행 할 수 있습니다.

    public void changeFragment(Type type, Fragment fragment){

        //화면 전환 프레그먼트 선언 및 초기화면 설정
        //프레그먼트 매니저로 추가, 삭제, 대체 가능
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //애니메이션 추가 - 이 메소드 실행 할 때 레이아웃이 오른쪽으로 사라지면서 바꿈.
        transaction.setCustomAnimations(R.anim.slide_in_right_left,              R.anim.fragment_close_exit);
type.ordinal()은 enum메소드 안에 있는 아이템들에 정의된 순서대로 리턴한다. 1보다 작으면 해당 프래그먼트로 대체하고 backstack에 저장하지 않고, 1보다 클시 백스텍에 저장해서 뒤로가기가 가능하다.
        
        if (type.ordinal() <=1) {
            transaction.replace(R.id.contentFrame, fragment).commit();
        }else if(type.ordinal() > 1){
            //해당 transaction 을 Back Stack 에 저장
            transaction.addToBackStack(null);
            transaction.replace(R.id.contentFrame, fragment).commit();
        }
    }

## Author

👤 **이봉희(BongHee Lee)**

- Github: [@maxbongbong](https://github.com/maxbongbong) 
