package com.example.pc_house;

import android.app.Activity;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private CartAdapter cartAdapter;

  Context context;


  @Before
    public  void setUp(){
      cartAdapter=new CartAdapter();
  }

  @Test
    public void getUnit_iscorrect(){
      double result=cartAdapter.getUnitPrice(100.0,25);
   assertEquals(4,result,0.01);
  }

}