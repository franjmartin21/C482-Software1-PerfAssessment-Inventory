package com.fm.inventory.ui.custom;

import javafx.scene.control.TextField;

/**
 * Created by francisco on 5/29/17.
 */
public class IntegerTextField extends TextField{

    public IntegerTextField(){
        this.setPromptText("Enter only numbers");
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if(text.matches("[0-9]") || text.isEmpty()){
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
