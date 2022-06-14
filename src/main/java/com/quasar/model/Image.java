package com.quasar.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.codec.digest.DigestUtils;

import com.quasar.Constants;
import com.quasar.files.FileHandler;
import com.quasar.util.CrcUtil;

@Entity
@Table(
    name = "image"
)
public class Image implements Comparable<Image> {
    
    @Id
    @Column(
        length = 36,
        unique = true,
        nullable = false,
        name = "imageid"
    )
    private String imageId;
    @Column(name="albumid")
    private String albumId;
    private String name;
    private String path;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "next_image_id")
    private String nextId;
    @Column(name = "previous_image_id")
    private String previousId;
    @Column(name = "is_vertical")
    private boolean isVertical;
    @Column(name = "duplicate_of_imageid")
    private String duplicateOfImageId;
    @Column(name = "file_exists")
    private boolean fileExists;

    public Image() {}
    
    
    public Image(Image duplicate, String originalImageId, String originalPath) {
    	this.imageId = UUID.randomUUID().toString();
    	this.name = duplicate.name;
    	this.albumId = duplicate.getAlbumId();
    	this.duplicateOfImageId = originalImageId;
    	this.path = originalPath;
    }
    
    public Image(File file, String albumId, FileHandler fileHandler) {
        this.name = file.getName();
        this.path = file.getPath();
        this.albumId = albumId;
        
        try {  // TODO extract date taken from EXIF
            BasicFileAttributes attr = fileHandler.getFileAttributes(file);
            this.createdDate = new Date(attr.creationTime().toMillis());
        } catch (IOException ex) {
            this.createdDate = new Date(Calendar.getInstance().getTime().getTime());
            ex.printStackTrace();
        }

        try {
			this.imageId = DigestUtils.md5Hex(String.valueOf(CrcUtil.getFileCyclicRedundancyCheck(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public String getId() {
        return this.imageId;
    }

//    public void setId(String id) {
//        this.imageId = id;
//    }

    public String getAlbumId() {
        return this.albumId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public String getThumbnailPath() {
        return this.path.substring(0, this.path.length() - this.name.length()) + Constants.THUMBNAILS_DIR + File.separator + this.name;
    }

    public Date getDateTaken() {
        if (this.createdDate != null)
            return new Date(this.createdDate.getTime());
        else
            return new Date();
    }

    public String getNextId() {
        return this.nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getPreviousId() {
        return this.previousId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public boolean isVertical() {
		return isVertical;
	}
    
	public String getDuplicateOfImageId() {
		return duplicateOfImageId;
	}

	public void setDuplicateOfImageId(String duplicateOfImageId) {
		this.duplicateOfImageId = duplicateOfImageId;
	}

	public int compareTo(Image i) {
        int result = this.createdDate.compareTo(i.getDateTaken());
        return result != 0 ? result : this.getName().compareTo(i.name);
    }

    public boolean equals(Object image) {
        return this.getId().equals(((Image)image).getId());
    }

	public Image createRefToDuplicate(Image duplicateImage) {
		return new Image(duplicateImage, this.imageId, this.path);
	}


	@Override
	public String toString() {
		return "Image [imageId=" + imageId + ", albumId=" + albumId + ", name="
				+ name + ", path=" + path + ", createdDate=" + createdDate
				+ ", nextId=" + nextId + ", previousId=" + previousId
				+ ", isVertical=" + isVertical + ", duplicateOfImageId="
				+ duplicateOfImageId + "]";
	}

	public boolean fileExists() {
		return fileExists;
	}

	public void markAsNonExistentFile() {
		fileExists = false;
	}

}