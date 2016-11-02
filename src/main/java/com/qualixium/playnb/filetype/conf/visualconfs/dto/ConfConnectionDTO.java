package com.qualixium.playnb.filetype.conf.visualconfs.dto;

import java.util.Objects;

public class ConfConnectionDTO {

    public String driverName;
    public String url;
    public String username;
    public String password;

    public void cleanVariables() {
        if (driverName != null) {
            driverName = driverName.replace("\"", "").trim();
        }
        if (url != null) {
            url = url.replace("\"", "").trim();
        }
        if (username != null) {
            username = username.replace("\"", "").trim();
        }
        if (password != null) {
            password = password.replace("\"", "").trim();
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.driverName);
        hash = 79 * hash + Objects.hashCode(this.url);
        hash = 79 * hash + Objects.hashCode(this.username);
        hash = 79 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfConnectionDTO other = (ConfConnectionDTO) obj;
        if (!Objects.equals(this.driverName, other.driverName)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }

}
