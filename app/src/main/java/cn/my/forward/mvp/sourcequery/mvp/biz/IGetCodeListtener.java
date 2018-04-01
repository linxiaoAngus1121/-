package cn.my.forward.mvp.sourcequery.mvp.biz;


import java.io.InputStream;

import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;

/**
 * Created by 123456 on 2018/2/9.
 */

public interface IGetCodeListtener {
    void getCodeSuccess(InputStream inputStream, Bean_l beanL);

    void getCodeFailure(String s);

    void getViewStateError(String s);
}
