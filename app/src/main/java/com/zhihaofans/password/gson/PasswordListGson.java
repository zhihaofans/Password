package com.zhihaofans.password.gson;

import java.util.List;

/**
 * @author zhihaofans
 * @date 2017/12/29
 */
public class PasswordListGson {
    private List<PasswordGson> passwords;

    public List<PasswordGson> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<PasswordGson> passwords) {
        this.passwords = passwords;
    }
}
