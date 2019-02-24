package com.quasar.controllers.dto;

import com.quasar.model.Album;

public class AlbumWithPermissions implements Comparable<AlbumWithPermissions> {

	private Album album;
	private boolean albumPermission;
	
	public Album getAlbum() {
		return album;
	}
	public boolean getAlbumPermission() {
		return albumPermission;
	}
	
	public AlbumWithPermissions(Album album, boolean albumPermission) {
		this.album = album;
		this.albumPermission = albumPermission;
	}
	@Override
	public int compareTo(AlbumWithPermissions o) {
		// TODO Auto-generated method stub
		return album.getName().compareTo(o.getAlbum().getName());
	}
}
