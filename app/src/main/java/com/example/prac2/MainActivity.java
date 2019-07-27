package com.example.prac2;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.enableDefaults();

        TextView status1 = (TextView)findViewById(R.id.result); //파싱된 결과확인

        boolean initem=false,inaddr1=false,inaddr2 = false, indist = false, intitle=false;

        String addr1 = null, addr2 = null, dist = null, title=null;
        String key="yOv5P9kxcP5VnWt8txP86aulztqNFQ1Tx848A4ZIyOgQCl0nnx6Zgp2iZO768lX2VyqpNqF8eXFYosPaxm6z%2FQ%3D%3D";
        double xpos=126.9815706850;
        double ypos=37.5685207373;


        try{
            URL url = new URL("https://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey="
                    +key+"&mapX="+xpos+"&mapY="+ypos+"&radius=1000&listYN=Y&arrange=A&MobileOS=ETC&MobileApp=AppTest"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("addr1")){ //addr1 만나면 내용을 받을수 있게 하자
                            inaddr1 = true;
                        }
                        if(parser.getName().equals("addr2")){ //addr2 만나면 내용을 받을수 있게 하자
                            inaddr2 = true;
                        }
                        if(parser.getName().equals("dist")){ //dist 만나면 내용을 받을수 있게 하자
                            indist = true;
                        }
                        if(parser.getName().equals("title")){ //title 만나면 내용을 받을수 있게 하자
                            intitle = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inaddr1){ //isaddr1이 true일 때 태그의 내용을 저장.
                            addr1 = parser.getText();
                            inaddr1 = false;
                        }
                        if(inaddr2){ //inaddr2이 true일 때 태그의 내용을 저장.
                            addr2 = parser.getText();
                            inaddr2 = false;
                        }
                        if(indist){ //indist이 true일 때 태그의 내용을 저장.
                             dist= parser.getText();
                            indist = false;
                        }
                        if(intitle){ //inMapx이 true일 때 태그의 내용을 저장.
                            title= parser.getText();
                            intitle = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            status1.setText(status1.getText()+"지명 : "+ addr1 +"\n 주소: "+ addr2 +"\n 거리 : " + dist + "m\n 제목 : " + title
                                    +"\n\n ");
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("에러가..났습니다...");
        }
    }
}