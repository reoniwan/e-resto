package com.frozenproject.aplikasipesanmakan.model;

import java.util.List;

public class FoodModel {
    private String name, image, id, description;
    private Long price;
    private List<AddOnModel> addon;
    private List<SizeModel> size;

    //For Cart
    private List<AddOnModel> userSelectedAddOn;
    private SizeModel userSelectedSize;

    public FoodModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<AddOnModel> getAddon() {
        return addon;
    }

    public void setAddon(List<AddOnModel> addon) {
        this.addon = addon;
    }

    public List<SizeModel> getSize() {
        return size;
    }


    public void setSize(List<SizeModel> size) {
        this.size = size;
    }

    public List<AddOnModel> getUserSelectedAddOn() {
        return userSelectedAddOn;
    }

    public void setUserSelectedAddOn(List<AddOnModel> userSelectedAddOn) {
        this.userSelectedAddOn = userSelectedAddOn;
    }

    public SizeModel getUserSelectedSize() {
        return userSelectedSize;
    }

    public void setUserSelectedSize(SizeModel userSelectedSize) {
        this.userSelectedSize = userSelectedSize;
    }
}
