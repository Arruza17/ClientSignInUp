/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Hyperlink;

/**
 *
 * @author jorge
 */
public class FacilityTableBean {
    private Long id;

    private final SimpleStringProperty type;

    private SimpleObjectProperty<Date> adqDate;

    public FacilityTableBean(Facility f) {
        this.id= f.getId();
        this.type =new SimpleStringProperty(f.getType());
        this.adqDate= new SimpleObjectProperty<>(f.getAdquisitionDate());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   

    

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Date getAdqDate() {
        return adqDate.get();
    }

    public void setAdqDate(Date date) {
        this.adqDate.set(date);
    }

    public Hyperlink getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(Hyperlink moreInfo) {
        this.moreInfo = moreInfo;
    }
    
}
