package com.play.pojo;

public class UserRegisteration {
	private String nickname;
	private String username;
    private String telephone;
    private String password;
    private String interest;

    public UserRegisteration() {
    }

    
    public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username   ;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

	public String getInterest() {
		return interest;
	}


	public void setInterest(String interest) {
		this.interest = interest;
	}


	@Override
	public String toString() {
		return "UserRegisteration [username=" + username + ", telephone=" + telephone + ", password=" + password + "]";
	}
    
}
