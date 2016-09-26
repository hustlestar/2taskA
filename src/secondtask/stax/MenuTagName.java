package secondtask.stax;

public enum MenuTagName {
    MENU, SUBMENU, DISH, PHOTO, TITLE, DESCRIPTION, WEIGHT, PRICE;

    public static MenuTagName getElementTagName(String element) {
        switch (element) {
            case "menu":
                return MENU;
            case "submenu":
                return SUBMENU;
            case "dish":
                return DISH;
            case "photo":
                return PHOTO;
            case "title":
                return TITLE;
            case "description":
                return DESCRIPTION;
            case "weight":
                return WEIGHT;
            case "price":
                return PRICE;
            default:
                throw new EnumConstantNotPresentException(MenuTagName.class, element);
        }
    }

}
