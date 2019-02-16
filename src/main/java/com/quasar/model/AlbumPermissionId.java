package com.quasar.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AlbumPermissionId implements Serializable {

	private static final long serialVersionUID = 8439514396323751924L;

	public AlbumPermissionId() {}

    public AlbumPermissionId(String userid, String albumid) {
        this.userId = userid;
        this.albumId = albumid;
    }
    
	@Column(name="userid")
	private String userId;
	
	@Column(name="albumid")
	private String albumId;


    public String getUserId() {
        return userId;
    }

    void setUserId(String id) {
        this.userId = id;
    }

	public String getAlbumId() {
		return albumId;
	}

	void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	@Override
	public String toString() {
		return "AlbumPermissionId [userId=" + userId + ", albumId=" + albumId
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumId == null) ? 0 : albumId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlbumPermissionId other = (AlbumPermissionId) obj;
		if (albumId == null) {
			if (other.albumId != null)
				return false;
		} else if (!albumId.equals(other.albumId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


}
	