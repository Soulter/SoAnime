package com.soulter.soanime;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.github.jorgecastilloprz.FABProgressCircle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.os.Looper;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnimeSearchActivity extends AppCompatActivity {


    private String imageBase64;
    private FABProgressCircle fabProgressCircle;
    private FloatingActionButton fab;
    private ImageView displayImage;

    private ViewPager animeViewPager;
    private ArrayList<Fragment> animePagerList;
    private ArrayList<String> aniTokenthumb;
    private ArrayList<String> aniAt;
    private ArrayList<String> aniId;
    private ArrayList<String> aniFilename;
    private FragmentAdapter mAdapter;
    private RelativeLayout mainAbout;
    private TabLayout tabLayout;
    private ImageView imageResult;
    private ProgressBar imageProg;
    private Toolbar toolbar;
//    final SharedPreferences spfs = this.getSharedPreferences("spfs", Context.MODE_PRIVATE);
//    final SharedPreferences.Editor editor = this.getSharedPreferences("spfs", Context.MODE_PRIVATE).edit();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static final String ACTION_NAME = "com.soulter.soanime.AnimeSearchActivity.COUNTER_ACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        fab = (FloatingActionButton) findViewById(R.id.search_anime_fab);
        fabProgressCircle = (FABProgressCircle)findViewById(R.id.fabProgressCircle);
        displayImage = (ImageView)findViewById(R.id.display_image);
        mainAbout = findViewById(R.id.main_about);

        animeViewPager = (ViewPager) findViewById(R.id.anime_pager);
        tabLayout = findViewById(R.id.tabLayout);

        animePagerList = new ArrayList<Fragment>();
        aniTokenthumb = new ArrayList<>();//new一个List<Fragment>
        aniAt = new ArrayList<>();
        aniFilename = new ArrayList<>();
        aniId = new ArrayList<>();

        animePagerList.add(new AnimeResultFragment());
        imageResult = findViewById(R.id.image_result);
        imageProg = findViewById(R.id.image_result_progbar);

        mAdapter = new FragmentAdapter(getSupportFragmentManager(), animePagerList);
//        animeViewPager.setAdapter(mAdapter);
//        animeViewPager.setCurrentItem(0);

        animeViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tagtag","onononn"+tab.getPosition());
                int position = tab.getPosition();

                try {
                    animeViewPager.setCurrentItem(position);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    ResultImageHandler(aniId.get(position),aniTokenthumb.get(position),aniFilename.get(position),aniAt.get(position));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectImage();

            }
        });
//
//        wvBookPlay.getSettings().setJavaScriptEnabled(true);
//        wvBookPlay.getSettings().setUseWideViewPort(true);
//        wvBookPlay.getSettings().setLoadWithOverviewMode(true);
//        wvBookPlay.getSettings().setAllowFileAccess(true);
//        wvBookPlay.getSettings().setSupportZoom(true);
//        wvBookPlay.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        try {
//            if (Build.VERSION.SDK_INT >= 16) {
//                Class<?> clazz = wvBookPlay.getSettings().getClass();
//                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
//                if (method != null) {
//                    method.invoke(wvBookPlay.getSettings(), true);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        wvBookPlay.getSettings().setPluginState(WebSettings.PluginState.ON);
//        wvBookPlay.getSettings().setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK
//        wvBookPlay.setWebChromeClient(new MyWebChromeClient());// 重写一下，有的时候可能会出现问题
//        wvBookPlay.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
//        wvBookPlay.getSettings().setSupportZoom(true);
//        wvBookPlay.getSettings().setBuiltInZoomControls(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            wvBookPlay.getSettings().setMixedContentMode(wvBookPlay.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
//
//        }
//
//        String url = "https://media.trace.moe/video/20602/[CASO&SumiSora][Amaburi][12][BIG5][720P].mp4?t=930&token=xpAVHNHzxPmV1PMymdWDxA";
//
//        CookieManager cookieManager = CookieManager.getInstance();
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append("android");
//
//        cookieManager.setCookie(url, stringBuffer.toString());
//        cookieManager.setAcceptCookie(true);

//        wvBookPlay.loadUrl(url);



        checkUpdate();

    }

    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int vc = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            vc = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return vc;
    }

    //此方法为检查软件版本更新。可以自行修改，也可以删掉。
    public void checkUpdate(){

        Request request = new Request.Builder()
                .url("http://idoknow.top/soultertemp/soanime/app_update.html")
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tag", "onFailure: " + e.getMessage());
                Snackbar.make(animeViewPager, "自动检查更新失败~~", BaseTransientBottomBar.LENGTH_LONG).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("tag", response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("tag", headers.name(i) + ":" + headers.value(i));
                }
                try {
                    String result = response.body().string(); //我草泥马只能调用一次 操你妈
                    JSONObject jsonObject = new JSONObject(result);
                    if (Integer.valueOf(jsonObject.get("versionCode").toString()) > getVersionCode(AnimeSearchActivity.this)){
                        Looper.prepare();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimeSearchActivity.this);
                        builder.setTitle("有新版本啦")
                                .setMessage("新版本信息："+jsonObject.get("info"))
                                .setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("前往更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
                                        intent.setData(Uri.parse("http://idoknow.top/soultertemp/soanime/app_release.apk"));//为Intent设置数据
                                        startActivity(intent);//将Intent传递给Activity
                                    }
                                })
                        .setCancelable(false).create().show();
                        Looper.loop();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anime_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about){

            Intent intent = new Intent(AnimeSearchActivity.this,AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //    private class MyWebChromeClient extends WebChromeClient {
//        WebChromeClient.CustomViewCallback mCallback;
//        @Override
//        public void onShowCustomView(View view, CustomViewCallback callback) {
//            Log.i("ToVmp","onShowCustomView");
////            fullScreen();
//
//            wvBookPlay.setVisibility(View.GONE);
////            flVideoContainer.setVisibility(View.VISIBLE);
////            flVideoContainer.addView(view);
//            mCallback = callback;
//            super.onShowCustomView(view, callback);
//        }
//
//        @Override
//        public void onHideCustomView() {
//            Log.i("ToVmp","onHideCustomView");
////            fullScreen();
//
////            wvBookPlay.setVisibility(View.VISIBLE);
////            flVideoContainer.setVisibility(View.GONE);
////            flVideoContainer.removeAllViews();
//            super.onHideCustomView();
//
//        }
//    }

    private void startSelectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        } else {
            //已授权，获取照片
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intentToPickPic, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && data != null){
            Log.v("tag",data.getData().toString());
            fabProgressCircle.show();
//            fab.setEnabled(false);
            imageBase64 = urltoBase64(uri2url(data.getData()));
            displayImage.setImageURI(data.getData());
            uploadPhoto();



            Log.v("tag",""+imageBase64);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String uri2url(Uri uri){

        String filePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                final String[] divide = documentId.split(":");
                final String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }
                String selection = BaseColumns._ID + "=?";
                String[] selectionArgs = {divide[1]};
                filePath = getDataColumn(this, mediaUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(this, contentUri, null, null);
            }else if(isExternalStorageDocument(uri)) {
                String [] split = documentId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(this, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        Log.v("tag",""+filePath);
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public void uploadPhoto(){
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("image",imageBase64);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url("https://api.trace.moe/search")
                .post(RequestBody.create(mediaType, String.valueOf(json)))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tag", "onFailure: " + e.getMessage());
                if (e.getMessage() == "timeout"){
                    Snackbar.make(animeViewPager,"请求超时，重新上传嗷！！", BaseTransientBottomBar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(animeViewPager,e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("tag", response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("tag", headers.name(i) + ":" + headers.value(i));
                }
                String result = response.body().string(); //只能调用一次 fuck

                        fabProgressCircle.post(new Runnable() {
                            @Override
                            public void run() {
                                fabProgressCircle.beginFinalAnimation();
                            }
                        });


                analyzeResults(result);



                mAdapter = new FragmentAdapter(getSupportFragmentManager(), animePagerList);
                mainAbout.post(new Runnable() {
                    @Override
                    public void run() {
                        mainAbout.setVisibility(View.GONE);
                    }
                });
                animeViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        animeViewPager.setVisibility(View.VISIBLE);
                        animeViewPager.setAdapter(mAdapter);
                        animeViewPager.setCurrentItem(0);
                    }
                });

            }
        });
    }

    public void analyzeResults(String result){

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.removeAllTabs();
            }
        });


        try {//草泥马戈壁要try还不提示
            Log.v("lwl",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("docs");
            animePagerList.clear();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                Integer time = Integer.parseInt(jsonObject1.get("at").toString().split("\\.")[0]);
                int minute = 0;
                int second = 0;
                while (true) {
                    minute += 1;
                    if (60 * minute > time) {
                        second = 60 * minute - time;
                        break;
                    }

                }


                Float similarity = null;
                try {
                    similarity = Float.parseFloat(jsonObject1.get("similarity").toString())*100;
                }catch (Exception e){
                    e.printStackTrace();
                }


                String title = "番名：" + jsonObject1.get("title_chinese") +"\n";
                String title_non = jsonObject1.get("title_chinese").toString();
                String details = "剧集：第" + jsonObject1.get("episode") + "话" + "\n"
                        + "定位：第" + minute + "分" + second + "秒" + "\n"
                        + "上映：" + jsonObject1.get("season") + "\n"
                        + "相似度：" + similarity + "%\n";


                AnimeResultFragment fragmentAnime = new AnimeResultFragment();



                Bundle bundle = new Bundle();
                bundle.putString("anime_result_title", title);
                bundle.putString("anime_result_details", details);
                try {
                    bundle.putFloat("anime_result_similarity",similarity);
                }catch (Exception e){
                    e.printStackTrace();
                }

                //首先有一个Fragment对象 调用这个对象的setArguments(bundle)传递数据
                fragmentAnime.setArguments(bundle);
                animePagerList.add(fragmentAnime);

                tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.setVisibility(View.VISIBLE);
                        tabLayout.addTab(tabLayout.newTab().setText(title_non));
                    }
                });


            }
            toolbar.post(new Runnable() {
                @Override
                public void run() {
                    toolbar.setTitle("SoAnime("+jsonArray.length()+"个结果)");

                }
            });



            animeViewPager.post(new Runnable() {
                @Override
                public void run() {
                    animeViewPager.setAdapter(mAdapter);
                    animeViewPager.setCurrentItem(0);

                }
            });




        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void ResultImageHandler(String anilistId,String tokenthumb,String filename,String at){
        String url = "https://media.trace.moe/image/"+anilistId+"/"+filename+"?t="+at+"&token="+tokenthumb;

        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tag", "onFailure: " + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();

                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            }
        });
    }



    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String urltoBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        Log.v("tag",""+result);
        return result;
    }

}