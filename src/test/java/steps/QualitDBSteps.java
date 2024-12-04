package steps;

import io.cucumber.java.ru.И;
import org.example.exceptions.DuplicateProductException;
import org.junit.jupiter.api.Assertions;

import java.sql.*;

public class QualitDBSteps {
    Connection connection;

    @И("установлено соединение с БД по адресу {string} с параметрами {string} {string}")
    public void установлено_соединение_с_бд_по_адресу_с_параметрами(String url, String user, String pass) throws SQLException {
        connection = DriverManager.getConnection(url,user,pass);
    }
    @И("проведена проверка на существование строки в БД с параметрами {string} {typeValue} {booleanValue}")
    public void проведена_проверка_на_существование_строки_в_бд_с_параметрами(String name, String type, Boolean exotic) throws SQLException{
        String query_check_existing = "SELECT COUNT(FOOD_ID) FROM FOOD WHERE FOOD_NAME = ? AND FOOD_TYPE = ? AND FOOD_EXOTIC = ?";
        PreparedStatement existing_PS = connection.prepareStatement(query_check_existing);
        String type_db = type.equals("Овощ")?"VEGETABLE":"FRUIT";
        existing_PS.setString(1,name);
        existing_PS.setString(2, type_db);
        int exotic_code = exotic? 1:0;
        existing_PS.setInt(3, exotic_code);
        ResultSet resultSet = existing_PS.executeQuery();
        resultSet.first();
        int count = resultSet.getInt("COUNT(FOOD_ID)");
        Assertions.assertEquals(0, count,"Строка уже существует БД");
    }
    @И("проведена проверка, что строка с параметрами {string} {typeValue} {booleanValue} добавлена в БД")
    public void проведена_проверка_что_строка_с_параметрами_добавлена_в_бд(String name, String type, Boolean exotic) throws SQLException{
        String query_check_adding = "SELECT FOOD_TYPE, FOOD_EXOTIC FROM FOOD WHERE FOOD_NAME = ?";
        PreparedStatement adding_PS = connection.prepareStatement(query_check_adding);
        int exotic_code = exotic? 1:0;
        String type_db_expected = type.equals("Овощ")?"VEGETABLE":"FRUIT";
        adding_PS.setString(1, name);
        ResultSet resultSet1 = adding_PS.executeQuery();
        if (resultSet1.next()){
            String type_db = resultSet1.getString("FOOD_TYPE");
            int exotic_db = resultSet1.getInt("FOOD_EXOTIC");
            Assertions.assertAll("Проверка добавленной строки",
                    () -> Assertions.assertEquals(type_db_expected, type_db, "Тип товара не соответствует ожидаемому"),
                    () -> Assertions.assertEquals(exotic_code, exotic_db, "Экзотичность товара не соответствует ожидаемой"));
        }else{
            Assertions.fail("Строка не добавилась в БД");
        }
    }
    @И("удаление строки с параметрами {string} {typeValue} {booleanValue} из БД")
    public void удаление_строки_с_параметрами_из_бд(String name, String type, Boolean exotic) throws SQLException{
        String delete_query = "DELETE FROM FOOD WHERE FOOD_NAME = ? AND FOOD_TYPE = ? AND FOOD_EXOTIC = ?";
        PreparedStatement delete_PS = connection.prepareStatement(delete_query);
        String type_db = type.equals("Овощ")?"VEGETABLE":"FRUIT";
        int exotic_code = exotic?1:0;
        delete_PS.setString(1, name);
        delete_PS.setString(2, type_db);
        delete_PS.setInt(3, exotic_code);
        int rows = delete_PS.executeUpdate();
        Assertions.assertEquals(1, rows, "Ошибка при удалении данных");
    }
    @И("в бд добавлена строка с параметрами {string} {typeValue} {booleanValue}")
    public void в_бд_добавлена_строка_с_параметрами(String name, String type, Boolean exotic) throws SQLException{
        String insert_query = "INSERT INTO FOOD VALUES (DEFAULT,?,?,?)";
        PreparedStatement insert_PS = connection.prepareStatement(insert_query);
        String type_db = type.equals("Овощ")?"VEGETABLE":"FRUIT";
        int exotic_code = exotic?1:0;
        insert_PS.setString(1, name);
        insert_PS.setString(2, type_db);
        insert_PS.setInt(3, exotic_code);
        insert_PS.executeUpdate();
    }
    @И("выполнена проверка на наличие дубликата строки {string} {typeValue} {booleanValue}")
    public void выполнена_проверка_на_наличие_дубликата_строки(String name, String type, Boolean exotic) throws SQLException{
        String query_check_adding = "SELECT COUNT(*) FROM FOOD WHERE FOOD_NAME = ? AND FOOD_TYPE = ? AND FOOD_EXOTIC = ?";
        PreparedStatement adding_PS = connection.prepareStatement(query_check_adding);
        String type_db = type.equals("Овощ")?"VEGETABLE":"FRUIT";
        int exotic_code = exotic?1:0;
        adding_PS.setString(1, name);
        adding_PS.setString(2, type_db);
        adding_PS.setInt(3, exotic_code);
        ResultSet resultSet1 = adding_PS.executeQuery();
        resultSet1.first();

        DuplicateProductException exception = Assertions.assertThrows(DuplicateProductException.class, () ->{
            int count_duple = resultSet1.getInt(1);
            if (count_duple == 2){
                throw new DuplicateProductException("Дубликат товара найден в базе данных");
            }
        });
        Assertions.assertTrue(exception.getMessage().contains("Дубликат"));
    }
    @И("удаление строк с параметрами {string} {typeValue} {booleanValue} из БД")
    public void удаление_строк_с_параметрами_из_бд(String name, String type, Boolean exotic) throws SQLException{
        String delete_query = "DELETE FROM FOOD WHERE FOOD_NAME = ? AND FOOD_TYPE = ? AND FOOD_EXOTIC = ?";
        PreparedStatement delete_PS = connection.prepareStatement(delete_query);
        String type_db = type.equals("Овощ")?"VEGETABLE":"FRUIT";
        int exotic_code = exotic?1:0;
        delete_PS.setString(1, name);
        delete_PS.setString(2, type_db);
        delete_PS.setInt(3, exotic_code);
        int rows = delete_PS.executeUpdate();
        Assertions.assertEquals(2, rows, "Ошибка при удалении данных");
    }
    @И("выполнено постусловие DB")
    public void выполнено_постусловие_db(){
        QualitUISteps.выполнено_постусловие_ui();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
