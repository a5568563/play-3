package com.play.pojo;

public class CommentAdd {
	int nid;
	String content;
	
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentAdd [nid=" + nid + ", content=" + content + ", uid="  + "]";
	}
	
	
}
