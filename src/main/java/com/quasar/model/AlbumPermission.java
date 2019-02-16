package com.quasar.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "album_permissions")
public class AlbumPermission {
	
	@EmbeddedId
	private AlbumPermissionId id;
	
	@Column(name="created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name="deleted_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDate;
	
	public AlbumPermission(){}
	
	public AlbumPermission(AlbumPermissionId id, Date createdDate, Date deletedDate) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.deletedDate = deletedDate;
	}

	public AlbumPermissionId getId() {
		return id;
	}

	public void setId(AlbumPermissionId id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	@Override
	public String toString() {
		return "AlbumPermission [id=" + id + ", createdDate=" + createdDate
				+ ", deletedDate=" + deletedDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((deletedDate == null) ? 0 : deletedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AlbumPermission other = (AlbumPermission) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (deletedDate == null) {
			if (other.deletedDate != null)
				return false;
		} else if (!deletedDate.equals(other.deletedDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder {
		
		private String albumId;
		private String userId;
		private Date createdDate;
		private Date deletedDate;
		
		public Builder setAlbumId(String albumId) { this.albumId = albumId; return this; }
		public Builder setUserId(String userId) { this.userId = userId; return this; }
		public Builder setCreatedDate(Date createdDate) { this.createdDate = createdDate; return this; }
		public Builder setDeletedDate(Date deletedDate) { this.deletedDate = deletedDate; return this;	}
		
		public AlbumPermission build() {
			return new AlbumPermission(new AlbumPermissionId(this.userId, this.albumId), createdDate, deletedDate);
			
		}
	}
}