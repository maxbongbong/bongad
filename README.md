# ✨ bongad

## 🚀 Lotto Demo App

사용 된 스킬 : Retrofit, Room, Http3, Single Activity + Multiple Fragments, RecyclerView, Sort

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
>HistoryFragment

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

fragment_main 을 view로 리턴 

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }
onViewCreated에 버튼 리스너들 구현하고, generateBtn을 클릭시 - EditText인 tv_result의 텍스트 값이 변하는것에 따라 버튼과 레이아웃 구성합니다.
입력되는 EditText에 변화가 있으면 v_result의 할당된 구역과 레이아웃이 사라지고, generateBtn은 tv_result의 문자열 길이가 0이 아니면 활성화되고 0일시에 비활성화 된다.

        //생성하기 버튼
        Button generate = getView().findViewById(R.id.bt_generate);
        generate.setEnabled(false);
        tv_result = getView().findViewById(R.id.tv_event_number);
        View view1 = getView().findViewById(R.id.v_result);
        tv_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                view1.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() != 0) {
                    generate.setEnabled(true);
                }else{
                    generate.setEnabled(false);
                }
            }
        });

생성버튼 클릭 리스너 - EditText값을 int로 형변환한 값이 0보다 크고 51보다 작아야한다. 만약 1~50의 숫자가 아닐시 다이얼로그가 출력 되고,
1~50사이의 숫자 일시 tv_result가 출력 되고, 1등수를 확인 할 수 있는 result_bt(결과보기 버튼)이 나온다.

        //생성버튼 클릭 리스너
        generate.setOnClickListener(v -> {
            String drwNo = tv_result.getText().toString();
            int num = Integer.parseInt(drwNo);
            if (num > 0 && num < 51) {
                view1.setVisibility(View.VISIBLE);
                showToast();
                getLottoTicket();
                tv_generate = getView().findViewById(R.id.tv_lotto);
                tv_generate.setText(convertIntoString(Result));
            }else{
                if (flag) {
                    //다이얼로그 띄우기(1보다 작고 50보다 크면 출력)
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.notification);
                    builder.setMessage(R.string.possible);
                    builder.setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), R.string.check, Toast.LENGTH_LONG);
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            flag = true;
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    flag = false;
                }
            }
            //결과보기 버튼
            Button result_bt = getView().findViewById(R.id.bt_match);
            result_bt.setOnClickListener(v1 -> {
                callAPIs();
            });
            flag = true;
        });
    }
    
복수통신으로 retrofit클라이언트를 이용한 비동기 으로 당첨 번호를 ArrayList<Integer> temp넣어서 show메소드로 넣어줌

    void callAPIs() {

        int i = Integer.parseInt(tv_result.getText().toString());
        Apiservice apiService = new RetrofitMaker().createService(getActivity(), Apiservice.class);
        Call<Lotto> commentStr = apiService.getComment(i);
        commentStr.enqueue(new Callback<Lotto>() {
            @Override
            public void onResponse(Call<Lotto> call, Response<Lotto> response) {
                boolean isSuccessful = response.isSuccessful();
                if (isSuccessful) {
                    Lotto lotto = response.body();
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(lotto.drwtNo1);
                    temp.add(lotto.drwtNo2);
                    temp.add(lotto.drwtNo3);
                    temp.add(lotto.drwtNo4);
                    temp.add(lotto.drwtNo5);
                    temp.add(lotto.drwtNo6);
                    temp.add(lotto.bnusNo);

                    show(temp);
                }
            }
            @Override
            public void onFailure(Call<Lotto> call, Throwable t) {
            }
        });
    }

결과보기 버튼 클릭시 등수와 다이얼로그 출력.
    
    void show(List<Integer> Win){
        if (flag) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.result);

            String str = convertIntoString(Result);
            String str1 = convertIntoString(Win);
            String drwNo = tv_result.getText().toString();

            builder.setMessage("나의 번호 = [" + str + "]\n" + drwNo + "회번호 = [" + str1 + "]\n" + LottoRank(Win));
            builder.setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flag = true;
                    Toast.makeText(getActivity(),R.string.check, Toast.LENGTH_LONG).show();
                }
            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) { flag = true; }
            });
            builder.show();
            flag = false;
        }
    }
    
스트링 빌더를 이용해서 List<Integer>형식을 String형식으로 형변환 시켜주는 메소드.
     
    private String convertIntoString(List<Integer> change) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < change.size(); i++) {
            if (sb.length() > 0) {
                if (i == change.size() - 1) {
                    sb.append(" + ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(change.get(i));
        }
        return sb.toString();
    }
>MainActivity

Mainactivity생성시 activity_main 레이아웃을 view로 return하고, splashfragment를 띄워주고 툴바는 안보이게 합니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment splashFragment;
        splashFragment = new SplashFragment();
        Toolbar(0);
        changeFragment(Type.splash, splashFragment);
    }

툴바는 안드로이드 API level21부터 추가된 위젯으로, 기존에 있던 Appbar보다 버전에 따라 달라지는 파편호 문제를 해결하고, 호환성을 높이기 위해서 사용합니다.
Toolbar(int num)은 각각의 프래그먼트에 따라 setTitle로 제목을 바꾸고 어떤 화면인지 구별 해놓은 메소드입니다.

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

enum을 사용해서 코드가 단순해지며 가독성이 좋고, 인스턴스 생성과 상속을 방지합니다.

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

- Github: [@maxbongbong](https://github.com/maxbongbong) - https://github.com/maxbongbong 
