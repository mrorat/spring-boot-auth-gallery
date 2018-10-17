package com.quasar.repository;

import com.quasar.model.Album;
import com.quasar.model.Image;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.springframework.stereotype.Component;

@Component
public class Repository {
    private static Map<String, Album> mapOfAlbums = null;
    private static Map<String, Image> mapOfImages = null;

    public Repository() {
    }

    public static SortedSet<Album> getAlbums() {
        return mapOfAlbums != null ? new TreeSet<>(mapOfAlbums.values()) : null;
    }

    public static Album getAlbum(String albumId) {
        return (Album)mapOfAlbums.get(albumId);
    }

    public static synchronized void setAlbums(SortedSet<Album> _albums) {
        mapOfAlbums = new HashMap<>();
        Iterator<Album> var1 = _albums.iterator();

        while(var1.hasNext()) {
            Album album = (Album)var1.next();
            mapOfAlbums.put(album.getAlbumid(), album);
        }

    }

    public static String getAlbumPathById(String albumId) {
        if (mapOfAlbums.containsKey(albumId)) {
            return ((Album)mapOfAlbums.get(albumId)).getPath();
        } else {
            System.out.println("Map of albums not initialized!");
            return null;
        }
    }

    public static Image getImageById(String albumId, String imageId) {
        return (Image)((Album)mapOfAlbums.get(albumId)).getImages().get(imageId);
    }

    public static synchronized void addImages(List<Image> images) {
        if (mapOfImages == null) {
            mapOfImages = new TreeMap<>();
        }

        Iterator<Image> var1 = images.iterator();

        while(var1.hasNext()) {
            Image i = (Image)var1.next();
            mapOfImages.put(i.getId(), i);
        }

    }

    public static Set<Image> getImagesForAlbum(String albumId) {
        if (mapOfAlbums != null) {
        	Set<Image> images = new TreeSet<>(new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					// TODO Auto-generated method stub
					return o1.getDateTaken().compareTo(o2.getDateTaken());
				}
        		
			});
            images.addAll(((Album)mapOfAlbums.get(albumId)).getImages().values());
        	return images;
        } else {
            System.out.printf("Controller: getImagesForAlbum %s%n - album map is empty!", albumId);
            return new TreeSet<>();
        }
    }
}
