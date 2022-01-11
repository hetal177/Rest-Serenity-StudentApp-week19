package com.studentapp.studentinfo;

import com.studentapp.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

public class StudentCURDTestWithSteps {
    static String firstName = "Ram" + TestUtils.getRandomValue();
    static String lastName = "Sita" + TestUtils.getRandomValue();
    static String programme = "Api Testing";
    static String email = TestUtils.getRandomValue() + "raja@gmail.com";
    static int studentId;

    @Steps
    StudentSteps studentSteps;

    @Title("This Will create a new student")
    @Test
    public void test001() {
        List<String> courseList = new ArrayList<>();
        courseList.add("jira");
        courseList.add("selenium");
        ValidatableResponse response = studentSteps.createStudent(firstName, lastName, email, programme, courseList);
        response.log().all().statusCode(201);
    }

    @Title("Verify if the student was added to the application")
    @Test
    public void test002() {

        HashMap<String, Object> value = studentSteps.getStudentInfoByFirstname(firstName);
        Assert.assertThat(value, hasValue(firstName));
        studentId = (int) value.get("id");
    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test003() {
        firstName = firstName + "_updated";
        List<String> courseList = new ArrayList<>();
        courseList.add("Scala");
        courseList.add("Java");
        studentSteps.updateStudent(studentId, firstName, lastName, email, programme, courseList).log().all().statusCode(200);
        HashMap<String, Object> value = studentSteps.getStudentInfoByFirstname(firstName);
        Assert.assertThat(value, hasValue(firstName));
    }

    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test004() {
        studentSteps.deleteStudent(studentId).statusCode(204);
        studentSteps.getStudentById(studentId).statusCode(404);
    }
}