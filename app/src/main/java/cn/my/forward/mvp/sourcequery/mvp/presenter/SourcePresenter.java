package cn.my.forward.mvp.sourcequery.mvp.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.my.forward.R;
import cn.my.forward.customview.MyTextView;
import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;
import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;
import cn.my.forward.mvp.sourcequery.mvp.biz.IExamListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.IGetCodeListtener;
import cn.my.forward.mvp.sourcequery.mvp.biz.ILePaiListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.ILevelListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.ILogin;
import cn.my.forward.mvp.sourcequery.mvp.biz.IOnLoginListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.IOnQuerySourceListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.IPersonListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.ITickedListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.ITimeTableListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.SourceAndLoginBiz;
import cn.my.forward.mvp.sourcequery.mvp.utils.MyLog;
import cn.my.forward.mvp.sourcequery.mvp.view.IExamView;
import cn.my.forward.mvp.sourcequery.mvp.view.ILePaiView;
import cn.my.forward.mvp.sourcequery.mvp.view.ILevealView;
import cn.my.forward.mvp.sourcequery.mvp.view.ILoginView;
import cn.my.forward.mvp.sourcequery.mvp.view.IPersonView;
import cn.my.forward.mvp.sourcequery.mvp.view.ISourceView;
import cn.my.forward.mvp.sourcequery.mvp.view.ITicketsView;
import cn.my.forward.mvp.sourcequery.mvp.view.ITimeTableView;

/**
 * Created by 123456 on 2018/2/9.
 * presenter是view层和model层交互的桥梁
 */

public class SourcePresenter {
    private static ILogin sourceQuery = SourceAndLoginBiz.getInstance();
    private ISourceView view;
    private ITimeTableView mTableView;
    private IExamView mExamView;
    private ILevealView mLevel;
    private IPersonView mPerson;
    private ITicketsView ticketsView;
    private ILoginView mLoginView;
    private ILePaiView lpView;
    //private IQuestionView questionview;
    private Bean_l bean01;
    private Handler mhalder = new Handler(Looper.getMainLooper());  //让handler运行在主线程中

    //采用泛型进行定义，解决传入的类型不对
    public <E> SourcePresenter(E view) {
        if (view instanceof ITimeTableView) {
            mTableView = (ITimeTableView) view;
        } else if (view instanceof ISourceView) {
            this.view = (ISourceView) view;
        } else if (view instanceof IExamView) {
            this.mExamView = (IExamView) view;
        } else if (view instanceof ILevealView) {
            mLevel = (ILevealView) view;
        } else if (view instanceof IPersonView) {
            mPerson = (IPersonView) view;
        } else if (view instanceof ILoginView) {
            mLoginView = (ILoginView) view;
        } else if (view instanceof ITicketsView) {
            ticketsView = (ITicketsView) view;
        } else if (view instanceof ILePaiView) {
            lpView = (ILePaiView) view;
        } else {
            try {
                throw new Exception("大哥，你错了");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



  /*  public void question() {
        sourceQuery.questionQuery(new IQuestionListener() {
            @Override
            public void questionSuccess(final List<QuestionBean> list) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        questionview.showSelect(list);
                    }
                });
            }

            @Override
            public void questionError() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        questionview.showError();
                    }
                });
            }
        });
    }*/

    /**
     * 提交用户填写的信息
     *
     * @param mlist 信息集
     */
 /*   public void questionSubmit(List<String> mlist) {
        sourceQuery.questionSubmit(mlist, new ISubmitListener() {
            @Override
            public void submitSuccess() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        questionview.submitSuccess();
                    }
                });
            }

            @Override
            public void submitError() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        questionview.submitError();
                    }
                });
            }
        });
    }*/

    /**
     * 等级考试查询
     */
    public void LevelQuery() {
        sourceQuery.levelQuery(new ILevelListener() {
            @Override
            public void showResultSucceed(final ArrayList<LevelBean> been) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLevel != null) {
                            mLevel.showLevelData(been);
                        }
                    }
                });
            }

            @Override
            public void showResultError(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLevel != null) {
                            mLevel.showLevelDataError(s);
                        }
                    }
                });
            }
        });
    }

    /**
     * 个人信息
     */
    public void person() {
        mPerson.loading();
        sourceQuery.personInfomation(new IPersonListener() {
            @Override
            public void onPersonQuerySuccess(final BeanPerson person) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mPerson != null) {
                            mPerson.showData(person);
                        }
                    }
                });
            }

            @Override
            public void onPersonQueryError() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mPerson != null) {
                            mPerson.showDataError();
                        }

                    }
                });
            }
        });
    }


    /**
     * 考试查询
     *
     * @param postion 第几年第几个学期
     */
    public void examQuery(String postion) {
        sourceQuery.examQuery(postion, new IExamListener() {
            @Override
            public void showExamSuccess(final List<ExamBean> list) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mExamView != null) {
                            mExamView.showExam(list);
                        }


                    }
                });
            }

            @Override
            public void showExamError(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mExamView != null) {
                            mExamView.showError(s);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取viewstate还有其他登录所需要的信息。
     */
    public void prepareLogin() {
        sourceQuery.prepareLogin(new IGetCodeListtener() {
            @Override
            public void getCodeSuccess(final InputStream inputStream, Bean_l bean) {
                bean01 = bean;
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoginView != null) {
                            mLoginView.showCode(inputStream);
                        }
                    }
                });

            }


            @Override
            public void getCodeFailure(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoginView != null) {
                            mLoginView.showCodeError(s);
                        }
                    }
                });

            }

            @Override
            public void getViewStateError(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoginView != null) {
                            mLoginView.showViewStateError(s);
                        }
                    }
                });

            }
        });
    }

    /**
     * 火车票查询
     */
    public void ticket() {
        ticketsView.loading();
        String from = ticketsView.getFrom();
        String to = ticketsView.getTo();
        sourceQuery.tickets(from, to, new ITickedListener() {
            @Override
            public void getDataSuccess(final Bean_ticket ticket, final Bean_SpareTicket
                    spareTicket) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        ticketsView.showTicketData(ticket, spareTicket);
                    }
                });
            }

            @Override
            public void getDataError() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        ticketsView.showTicketError();
                    }
                });
            }
        });
    }


    /**
     * 登陆
     */
    public void login() {
        mLoginView.closekeyboard();
        bean01.setStuNo(mLoginView.getstudNo());
        bean01.setStuPs(mLoginView.getstuPs());
        bean01.setCode(mLoginView.getCode());
        MyLog.i(bean01.toString());
        sourceQuery.login(bean01, new IOnLoginListener() {
            @Override
            public void OnLoginSuccess(final String name) {
                mhalder.post(new Runnable() {//这样view.showLoginSuccess()方法就会执行在主线程中
                    @Override
                    public void run() {
                        if (mLoginView != null) {
                            mLoginView.showLoginSuccess(name);
                        }
                    }
                });
            }

            @Override
            public void OnLoginError() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoginView != null) {
                            mLoginView.showLoginError();
                        }
                    }
                });
            }
        });
    }

    /**
     * 点击了验证码
     */
    public void changeCode() {
        this.prepareLogin();
    }

    /**
     * 查询成绩
     *
     * @param year 根据的年份
     */
    public void scoureQuery(String year) {
        sourceQuery.score(year, new IOnQuerySourceListener() {
            @Override
            public void OnQuerySuccess(final ArrayList<String> list) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.showSource(list);
                        }

                    }
                });

            }

            @Override
            public void OnError() {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.showSourceError();
                        }
                    }
                });

            }
        });
    }

    /**
     * 课表查询
     *
     * @param start 第几周
     */
    public void timetable(int start) {

        sourceQuery.timeTable(start, new ITimeTableListener() {
            @Override
            public void QueryTimeTableSuccess(final List nodes) {
                MyLog.i("课表查询成功");
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTableView != null) {
                            mTableView.showTimeTble(nodes);
                        }
                    }
                });
            }

            @Override
            public void QuertTimeTableFailure(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTableView != null) {
                            mTableView.showTimeTbleError(s);
                        }
                    }
                });
            }
        });
    }

    /**
     * 颜值评分
     *
     * @param path 文件路径
     */
    public void lePai(String path) {
        lpView.showDialog();
        sourceQuery.lepai(path, new ILePaiListener() {
            @Override
            public void getDataSuccess(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        lpView.showInformationSuccess(s);
                    }
                });
            }

            @Override
            public void getCodeFailure(final String s) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        lpView.showInformationFailure(s);
                    }
                });
            }
        });
    }

    /**
     * 清除持有view的引用
     */
    public void clearAll() {
        if (mLoginView != null) {
            mLoginView = null;
        }
        if (view != null) {
            view = null;
        }
        if (mTableView != null) {
            mTableView = null;
        }
        if (mExamView != null) {
            mExamView = null;
        }
        if (mLevel != null) {
            mLevel = null;
        }
        if (mPerson != null) {
            mPerson = null;
        }
        if (ticketsView != null) {
            ticketsView = null;
        }
        if (lpView != null) {
            lpView = null;
        }
    }

    /**
     * 交换from和to
     *
     * @param from from
     * @param to   to
     * @return 结果
     */
    public String[] swap(String from, String to) {
        return new String[]{to, from};
    }

    public String getImagePath(Context context, Uri externalContentUri, String selection) {
        String path = null;
        Cursor cursor = context.getApplicationContext().getContentResolver().query
                (externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    /**
     * 生成历年成绩spinner适配器所需要的数据
     *
     * @param context 上下文
     * @param stuNo   根据学号
     * @param flag    是否要添加历年成绩几个字，用于区分是历年成绩的还是考试查询的,true则添加，false则不添加
     */
    public ArrayAdapter<String> initDataForAdapter(Context context, String stuNo, boolean flag) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout
                .simple_spinner_item, getList(stuNo.substring(0, 2), flag));
        adapter.setDropDownViewResource(R.layout.activity_exam_sp_dropdown);
        return adapter;
    }


    /**
     * 返回历年成绩的头部数据源
     *
     * @param stuNo 学号
     * @param flag  是否要添加历年成绩几个字，用于区分是历年成绩的还是考试查询的,true则添加，false则不添加
     * @return 返回数据源list
     */
    private ArrayList<String> getList(String stuNo, boolean flag) {
        ArrayList<String> list = new ArrayList<>();
        if (flag) {
            list.add("全部成绩");
        }
        int j = Integer.valueOf(stuNo) + 2000;    //2015
        for (int i = j + 2; i >= j; i--) { //2018开始
            for (int h = 2; h > 0; h--) {
                String line = String.valueOf(i) + "-" + String.valueOf(i + 1) + "-" + String
                        .valueOf(h);
                list.add(line);
            }
        }
        return list;
    }

    /**
     * 动态生成头部周几的代码，自定义一个view，圈圈
     */
    public void createTextView(Context context, LinearLayout layout) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams
                .MATCH_PARENT, 1.0f);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i1 = calendar.get(Calendar.DAY_OF_WEEK);    //获取当前是周几
        for (int i = 1; i <= 6; i++) {
            MyTextView mTv = new MyTextView(context);
            if (i == i1 - 1) {
                mTv.isToday = true;         //自定义view中的属性
            }
            mTv.setLayoutParams(lp);
            mTv.setGravity(Gravity.CENTER);
            mTv.setText(String.valueOf(i));
            mTv.setTextSize(18);
            layout.addView(mTv);
        }
    }


}
