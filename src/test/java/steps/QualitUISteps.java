package steps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.И;
import org.example.managers.DriverManager;
import org.example.pages.FoodPage;
import org.example.utils.Locators;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class QualitUISteps {
    static private DriverManager driverManager = DriverManager.getDriverManager();
    static private FoodPage foodPage;
    private WebDriverWait webDriverWait;
    private List<WebElement> table_rows;
    @И("инициализирован WebDriver")
    public void инициализирован_web(){
        webDriverWait = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(5));
        foodPage = new FoodPage(driverManager.getDriver());
    }
    @И("открыта страница по адресу {string}")
    public void открыта_страница_по_адресу(String url){
        driverManager.getDriver().get(url);
    }
    @ParameterType(value = "true|false")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }
    @ParameterType(value = "Овощ|Фрукт")
    public String typeValue(String value) {
        return value;
    }
    @И("проведена проверка на существование записи в таблице UI с параметрами {string} {typeValue} {booleanValue}")
    public void проведена_проверка_на_существование_записи_в_таблице_ui_с_параметрами_овощ_false(String name, String type, Boolean exotic){
        table_rows = foodPage.getTableRows();
        WebElement name_r, type_r, exotic_r;
        final boolean[] exist = {false};
        for (WebElement row:table_rows) {
            name_r = row.findElement(Locators.FoodPage.TABLE_ROW_NAME);
            type_r = row.findElement(Locators.FoodPage.TABLE_ROW_TYPE);
            exotic_r = row.findElement(Locators.FoodPage.TABLE_ROW_EXOTIC);
            if (name_r.getText().equals(name)
                    && type_r.getText().equals(type) && exotic_r.getText().equals(String.valueOf(exotic))){
                exist[0] = true;
                break;
            }
        }
        Assertions.assertFalse(exist[0], "Строка уже существует UI");
    }
    @И("выполнено нажатие на {string}")
    public void выполнено_нажатие_на(String text){
        foodPage = text.equals("Добавить")? foodPage.clickAddBtn():foodPage.clickSaveBtn();
        if (text.equals("Сохранить")){
            webDriverWait.until(ExpectedConditions.elementToBeClickable(Locators.FoodPage.BTN_ADD));
        }
    }
    @И("поле \"Наименование\" заполняется значением {string}")
    public void поле_заполняется_значением(String name){
        foodPage = foodPage.fillNameField(name);
    }
    @И("значение экзотичности устанавливается в {booleanValue}")
    public void значение_экзотичности_устанавливается_в_false(Boolean exotic){
        foodPage = foodPage.setExotic(exotic);
    }
    @И("выполнена проверка количества строк в таблице")
    public void выполнена_проверка_количества_строк_в_таблице(){
        int initialRowCount = table_rows.size();
        webDriverWait.until(ExpectedConditions.numberOfElementsToBe(Locators.FoodPage.TABLE_ROWS, initialRowCount + 1));
        Assertions.assertEquals(initialRowCount + 1, foodPage.getTableRows().size(), "Строка не добавилась");
    }
    @И("проведено сравнение данных в последней строке таблицы с ожидаемыми: {string} {typeValue} {booleanValue}")
    public void проведено_сравнение_данных_в_последней_строке_таблицы_с_ожидаемыми_овощ_false(String name, String type, Boolean exotic){
        WebElement lastTableRow = foodPage.getTableRows().get(table_rows.size());
        WebElement name_field = lastTableRow.findElement(Locators.FoodPage.TABLE_ROW_NAME);
        WebElement type_field = lastTableRow.findElement(Locators.FoodPage.TABLE_ROW_TYPE);
        WebElement exotic_field = lastTableRow.findElement(Locators.FoodPage.TABLE_ROW_EXOTIC);
        Assertions.assertEquals(name, name_field.getText(), "Неверное название");
        Assertions.assertEquals(type, type_field.getText(), "Неверный тип");
        Assertions.assertEquals(String.valueOf(exotic), exotic_field.getText(), "Неверная экзотичность");
    }
    @И("в поле \"Тип\" устанавливается значение {typeValue}")
    public void в_поле_устанавливается_значение_фрукт(String type){
        foodPage = foodPage.setType(type);
    }
    @И("выполнено постусловие UI")
    public static void выполнено_постусловие_ui(){
        foodPage.clickNavBarDropDown().clickResetBtn();
        driverManager.quitDriver();
    }
}
