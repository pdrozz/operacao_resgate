package com.pdrozzsolucoes.gor.model;

import java.io.File;

public class ImagemAnexadaModel {

    private String path;
    private boolean selected=false;
    private File file;

    public ImagemAnexadaModel() {
    }

    public void delete(){
        file.delete();
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
