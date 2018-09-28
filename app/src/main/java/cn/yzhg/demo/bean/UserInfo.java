package cn.yzhg.demo.bean;

import java.util.List;

/**
 * 类 名: UserInfo
 * 作 者: yzhg
 * 创 建: 2018/9/27 0027
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public class UserInfo {


    /**
     * errcode : 10000
     * errmsg : 查询成功
     * data : {"UserLevel":[{"Id":1,"RoleName":"商户"},{"Id":2,"RoleName":"出纳(商户)"},{"Id":3,"RoleName":"财务(商户)"},{"Id":4,"RoleName":"店长(店铺)"},{"Id":5,"RoleName":"出纳(店铺)"},{"Id":6,"RoleName":"财务(店铺)"},{"Id":7,"RoleName":"收银员"},{"Id":8,"RoleName":"导购"}]}
     */

    private int errcode;
    private String errmsg;
    private DataBean data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<UserLevelBean> UserLevel;

        public List<UserLevelBean> getUserLevel() {
            return UserLevel;
        }

        public void setUserLevel(List<UserLevelBean> UserLevel) {
            this.UserLevel = UserLevel;
        }

        public static class UserLevelBean {
            /**
             * Id : 1
             * RoleName : 商户
             */

            private int Id;
            private String RoleName;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getRoleName() {
                return RoleName;
            }

            public void setRoleName(String RoleName) {
                this.RoleName = RoleName;
            }

            @Override
            public String toString() {
                return "UserLevelBean{" +
                        "Id=" + Id +
                        ", RoleName='" + RoleName + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
