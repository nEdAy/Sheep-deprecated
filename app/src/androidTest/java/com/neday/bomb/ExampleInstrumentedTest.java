package com.neday.bomb;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * 测试一些数据性的功能，比如加载网络数据
 * <p>
 * 测试SharedPerferences，测试数据库，测试函数等
 * <p>
 * 工具类的测试，比如验证时间，转化格式，正则验证等等
 * <p>
 * 测试函数需要为public
 * <p>
 * 测试函数需要添加@Test注解
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.neday.bomb", appContext.getPackageName());
    }
}
