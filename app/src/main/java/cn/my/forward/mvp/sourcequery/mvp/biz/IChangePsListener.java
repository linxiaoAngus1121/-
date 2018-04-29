package cn.my.forward.mvp.sourcequery.mvp.biz;

/**
 * Created by 123456 on 2018/4/21.
 * 修改密码回调监听
 */

public interface IChangePsListener {
    /**
     * 密码修改成功
     */
    void PsChangedSuccess();

    /**
     * 密码修改失败
     */
    void PsChangedFailed();
}
