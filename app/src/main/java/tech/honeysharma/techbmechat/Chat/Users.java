package tech.honeysharma.techbmechat.Chat;

/**
 * Created by Honey Sharma.
 */

public class Users {

    public String name;
    public String image;
    public String status;
    public String thumb_image;
    public String device_token;



    public Users(){

    }

    public Users(String name, String image, String status, String thumb_image , String device_token) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice_token() {
        return device_token;
    }
    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

}
