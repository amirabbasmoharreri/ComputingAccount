package com.abbasmoharreri.computingaccount;

import com.abbasmoharreri.computingaccount.pesiandate.DateConverter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void CalenderTool_isCorrect() {

        DateConverter dateConverter =new DateConverter( 2020,6,20 );
        DateConverter dateConverter1 =new DateConverter( 2020,8,30 );
        DateConverter dateConverter2 =new DateConverter( 2020,10,2 );
        DateConverter dateConverter3 =new DateConverter( 2020,12,31 );
        DateConverter dateConverter4 =new DateConverter( 2020,1,31 );
        DateConverter dateConverter5 =new DateConverter( 2020,3,15 );

        assertEquals( 4, 2 + 2 );
        assertEquals( "1399/3/31", dateConverter.getIranianDate() );
        assertEquals( "1399/6/9", dateConverter1.getIranianDate() );
        assertEquals( "1399/7/11", dateConverter2.getIranianDate() );
        assertEquals( "1399/10/11", dateConverter3.getIranianDate() );
        assertEquals( "1398/11/11", dateConverter4.getIranianDate() );
        assertEquals( "1398/12/25", dateConverter5.getIranianDate() );
    }

    @Test
    public void splitString_isCorrect(){
        String date="1399-03-20 22:10";
        String[] split=date.split( "-| |:" );
        assertEquals( "1399",split[0] );
        assertEquals( "03",split[1] );
        assertEquals( "20",split[2] );
        assertEquals( "22",split[3] );
        assertEquals( "10",split[4] );
    }
}