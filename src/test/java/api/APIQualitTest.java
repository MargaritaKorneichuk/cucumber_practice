package api;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.example.pojos.ProductPOJO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Класс для проведения API тестирования.
 * Проверяет добавление овощей и фруктов через API.
 * @author Margarita Korneichuk
 */
public class APIQualitTest {
    /**
     * Куки для авторизации.
     */
    private Cookies cookies;

    /**
     * Метод, выполняющийся перед каждым тестом.
     * Получает куки для авторизации с помощью GET запроса к /api/food.
     */
    @BeforeEach
    public void preCondition() {
        cookies = given()
                .baseUri("https://qualit.applineselenoid.fvds.ru/api/")
                .contentType(ContentType.JSON)
                .when()
                .get("food")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getDetailedCookies();
    }

    /**
     * Параметризованный тест для проверки добавления овоща.
     * Использует данные из CsvSource.
     * @param name Название овоща.
     * @param exotic Является ли овощ экзотическим.
     */
    @ParameterizedTest
    @CsvSource({"Картофель,false","Melotria,true"})
    void testAddVegetable(String name, boolean exotic){

        // Устанавливаем спецификации для запросов и ответов
        Specifications.installSpecification(Specifications.requestSpecification("https://qualit.applineselenoid.fvds.ru/api/"),
                Specifications.responseSpecification(200));

        // Создаем объект ProductPOJO для тестирования
        ProductPOJO example = new ProductPOJO(name, "VEGETABLE",exotic);

        // Получаем список овощей до добавления
        List<ProductPOJO> list = given()
                .cookies(cookies)
                .when()
                .get("food")
                .then()
                .extract()
                .jsonPath()
                .getList("",ProductPOJO.class);

        // Проверяем, что овощ ещё не существует
        Assertions.assertFalse(list.contains(example), "Строка уже существует");

        // Добавляем овощ через POST запрос
        given()
                .cookies(cookies)
                .body("{\n" +
                        "  \"name\": \"" + name +"\",\n" +
                        "  \"type\": \"VEGETABLE\",\n" +
                        "  \"exotic\": "+exotic+"\n" +
                        "}")
                .contentType("application/json")
                .when()
                .post("food")
                .then().log().all();

        // Получаем список овощей после добавления
        List<ProductPOJO> list_ = given()
                .cookies(cookies)
                .when()
                .get("food")
                .then()
                .extract()
                .jsonPath()
                .getList("",ProductPOJO.class);

        // Проверяем, что овощ был добавлен
        Assertions.assertTrue(list_.contains(example),"Строка не добавилась");
    }

    /**
     * Параметризованный тест для проверки добавления фрукта.
     * Использует данные из CsvSource.
     * @param name Название фрукта.
     * @param exotic Является ли овощ экзотическим.
     */
    @ParameterizedTest
    @CsvSource({"Клубника,false","Mangosteen,true"})
    void testAddFruit(String name, boolean exotic){

        // Устанавливаем спецификации для запросов и ответов
        Specifications.installSpecification(Specifications.requestSpecification("https://qualit.applineselenoid.fvds.ru/api/"),
                Specifications.responseSpecification(200));

        // Создаем объект ProductPOJO для тестирования
        ProductPOJO example = new ProductPOJO(name,"FRUIT",exotic);

        // Получаем список фруктов до добавления
        List<ProductPOJO> list = given()
                .cookies(cookies)
                .when()
                .get("food")
                .then()
                .extract()
                .jsonPath()
                .getList("",ProductPOJO.class);

        // Проверяем, что фрукт ещё не существует
        Assertions.assertFalse(list.contains(example), "Строка уже существует");

        // Добавляем фрукт через POST запрос
        given()
                .cookies(cookies)
                .body("{\n" +
                        "  \"name\": \"" + name +"\",\n" +
                        "  \"type\": \"FRUIT\",\n" +
                        "  \"exotic\": "+exotic+"\n" +
                        "}")
                .contentType("application/json")
                .when()
                .post("food")
                .then().log().all();

        // Получаем список фруктов после добавления
        List<ProductPOJO> list_ = given()
                .cookies(cookies)
                .when()
                .get("food")
                .then()
                .extract()
                .jsonPath()
                .getList("",ProductPOJO.class);

        // Проверяем, что фрукт был добавлен
        Assertions.assertTrue(list_.contains(example),"Строка не добавилась");
    }

    /**
     * Метод, выполняющийся после каждого теста.
     * Сбрасывает данные через POST запрос к /data/reset.
     */
    @AfterEach
    void postCondition(){
        given()
                .baseUri("https://qualit.applineselenoid.fvds.ru/api/")
                .cookies(cookies)
                .when()
                .post("data/reset")
                .then().log().all()
                .statusCode(200);
    }
}
