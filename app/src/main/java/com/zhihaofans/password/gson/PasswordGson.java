package com.zhihaofans.password.gson;

/**
 * @author zhihaofans
 * @date 2017/12/18
 */
public class PasswordGson {
    /**
     * CreatTime : 0
     * EditTime : 1
     * Type : app
     * Account : user
     * Password : password
     * Title : App
     * Site : com.zhihaofans.android
     * Icon : icon
     */

    private int CreatTime = (int) System.currentTimeMillis() / 1000;
    private int EditTime = CreatTime;
    private String Type = "web";
    private String Account = "";
    private String Password = "";
    private String Title = "";
    private String Site = "";
    private String Icon = "";

    public String getAll() {
        return "CreatTime:" + Integer.toString(CreatTime) +
                "\nEditTime:" + Integer.toString(EditTime) +
                "\nType:" + Type +
                "\nAccount:" + Account +
                "\nPassword:" + Password +
                "\nTitle:" + Title +
                "\nSite:" + Site +
                "\nIcon:" + Icon;
    }

    public int getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(int CreatTime) {
        this.CreatTime = CreatTime;
    }

    public int getEditTime() {
        return EditTime;
    }

    public void setEditTime(int EditTime) {
        this.EditTime = EditTime;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getSite() {
        return Site;
    }

    public void setSite(String Site) {
        this.Site = Site;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }
}
