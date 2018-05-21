package cn.my.forward.mvp.sourcequery.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.my.forward.mvp.sourcequery.mvp.bean.BeanPerson;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_SpareTicket;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_l;
import cn.my.forward.mvp.sourcequery.mvp.bean.Bean_ticket;
import cn.my.forward.mvp.sourcequery.mvp.bean.ExamBean;
import cn.my.forward.mvp.sourcequery.mvp.bean.LevelBean;
import cn.my.forward.mvp.sourcequery.mvp.biz.IExamListener;
import cn.my.forward.mvp.sourcequery.mvp.biz.IGetCodeListtener;
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

    public void ticket() {
        ticketsView.loading();
        String from = ticketsView.getFrom();
        String to = ticketsView.getTo();
        sourceQuery.tickets(from, to, new ITickedListener() {
            @Override
            public void getDataSuccess(final Bean_ticket ticket, final Bean_SpareTicket spareTicket) {
                mhalder.post(new Runnable() {
                    @Override
                    public void run() {
                        ticketsView.showTicketData(ticket,spareTicket);
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

    public void changeCode() {
        this.prepareLogin();
    }

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
}
