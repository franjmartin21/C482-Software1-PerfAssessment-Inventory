package com.fm.inventory.ui.custom;

import javafx.scene.control.TextField;

/**
 * Created by francisco on 5/29/17.
 */
public class DecimalTextField extends TextField{
    public DecimalTextField(){
        this.setPromptText("Enter decimal numbers");
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if(text.matches("^\\d*\\.?\\d*$") || text.isEmpty()){
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}