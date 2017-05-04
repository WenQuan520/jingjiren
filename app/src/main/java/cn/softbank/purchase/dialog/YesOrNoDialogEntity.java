package cn.softbank.purchase.dialog;

public class YesOrNoDialogEntity {
    /***/
    public String titleOne;
    /***/
    public String btnCancelLabel;
    /***/
    public String btnOkLabel;

    public YesOrNoDialogEntity(String titleOne,
            String btnCancelLabel, String btnOkLabel) {
        super();
        this.titleOne = titleOne;
        this.btnCancelLabel = btnCancelLabel;
        this.btnOkLabel = btnOkLabel;
    }
}
