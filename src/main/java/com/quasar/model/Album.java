package com.quasar.model;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(
    name = "album"
)
public class Album implements Comparable<Album> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Album.class);

    @Id
    @Column(
        length = 36,
        unique = true,
        nullable = false
    )
    private String albumid;
    private String name;
    private String path;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
    private Date created_date;
    @Transient
    private Map<String, Image> images = new HashMap<>();

    public Album() {}
    
    public Album(File directory, List<Image> images) {
        this.albumid = UUID.randomUUID().toString();
        this.name = convertDirectoryNameToAlbumName(directory.getName());
        this.path = directory.getPath();
        Iterator<Image> var3 = images.iterator();

        while(var3.hasNext()) {
            Image i = (Image)var3.next();
            this.images.put(i.getId(), i);
        }

        this.updateCreatedDate();
    }
    
    public static String convertDirectoryNameToAlbumName(String directoryName) {
        return directoryName.replace("_", "-").replace("[", "(").replace("]", ")");
    }

//    private boolean isWhitespaceOrSimilar(char charAt) {
//        switch(charAt) {
//        case '\t':
//        case ' ':
//        case '-':
//        case '_':
//            return true;
//        default:
//            return false;
//        }
//    }

    public void setImages(List<Image> images) {
        LOGGER.info("Setting images for Album [name: %s], [id: %s], [image qty: %d]%n", this.getName(), this.getAlbumid(), images.size());
        Iterator<Image> var2 = images.iterator();

        while(var2.hasNext()) {
            Image i = (Image)var2.next();
            this.images.put(i.getId(), i);
        }

        this.updateCreatedDate();
    }

    private void updateCreatedDate() {
        if (this.created_date == null) {
            try {
            	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                this.created_date = formatter.parse(this.name.substring(0, 10));
            } catch (StringIndexOutOfBoundsException | IllegalArgumentException | ParseException var3) {
                Optional<String> firstKey = this.images.keySet().stream().findFirst();
                if (firstKey.isPresent()) {
                    this.created_date = ((Image)this.images.get(firstKey.get())).getDateTaken();
                    this.name = this.created_date.toString() + " " + this.name;
                } else {
                	this.created_date = new Date();
                }
            }
        }
    }

    public String getAlbumid() {
        return this.albumid;
    }

    public void setId(String id) {
        this.albumid = id;
        LOGGER.info("Setting id %s for album %s%n", id, this.name);
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public synchronized Date getCreated_date() {
        return this.created_date;
    }

    public int compareTo(Album o) {
        return this.name.compareTo(o.getName());
    }

    public Map<String, Image> getImages() {
        return this.images;
    }

    public void rename(File newNameAlbum) {
    	this.name = newNameAlbum.getName();
        this.path = newNameAlbum.getPath();
    }
}