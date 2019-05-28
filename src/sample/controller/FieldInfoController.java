package sample.controller;

import sample.view.res.ImageManager;

public class FieldInfoController {
    AppController appController;

    void bindParent(AppController appController) {
        this.appController = appController;

        ImageManager im = appController.getImageManager();

// Buttons from FieldInfo.fxml
//        bGenD.setGraphic(im.constructImageViewFrom(ImageManager.ICON_REFRESH, 16, 16, true));
//        bAccD.setGraphic(im.constructImageViewFrom(ImageManager.ICON_SHARE, 16, 16, true));
//        bModI.setGraphic(im.constructImageViewFrom(ImageManager.ICON_EDIT, 16, 16, true));
//        bSupI.setGraphic(im.constructImageViewFrom(ImageManager.ICON_REMOVE, 16, 16, true));
//        bCopN.setGraphic(im.constructImageViewFrom(ImageManager.ICON_COPY, 16, 16, true));
//        bSupN.setGraphic(im.constructImageViewFrom(ImageManager.ICON_REMOVE, 16, 16, true));
    }
}
