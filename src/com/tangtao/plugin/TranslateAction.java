package com.tangtao.plugin;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;

import java.awt.*;

public class TranslateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
       // Messages.showMessageDialog("Hello World","Information",Messages.getInformationIcon());
        final Editor mEditor=e.getData(PlatformDataKeys.EDITOR);
        if(null==mEditor){
            return;
        }
        SelectionModel model =mEditor.getSelectionModel();
        final String selectedText=model.getSelectedText();
        if(TextUtils.isEmpty(selectedText)){
            return;
        }

        String baseUrl="http://fanyi.youdao.com/openapi.do?"
                +"keyfrom=Skykai521&key=977124034&type=data&"
                +"doctype=json&version=1.1&q=";
        HttpUtils.doGetAsyn(baseUrl + selectedText, new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {

                Translation translation =new Gson().fromJson(result,Translation.class);
                showPopupBalloon(mEditor,translation.toString());
            }
        });

    }


    private void showPopupBalloon(final Editor editor,final String result){
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                JBPopupFactory factory=JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result,null,
                        new JBColor(new Color(186,238,186),
                        new Color(73,117,73)),null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor),
                                Balloon.Position.below);
            }
        });
    }
}
