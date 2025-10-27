package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> userDtoJacksonTester;

    @Autowired
    private JacksonTester<UserPatchDto> userPatchDtoJacksonTester;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void userDtoSerializationTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setLogin("johndoe");
        userDto.setBirthday(LocalDate.of(1990, 1, 1));

        JsonContent<UserDto> result = userDtoJacksonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathStringValue("$.login").isEqualTo("johndoe");
        assertThat(result).extractingJsonPathStringValue("$.birthday").isEqualTo("1990-01-01");
    }

    @Test
    void userDtoDeserializationTest() throws Exception {
        String json = "{\"id\": 1, "
                + "\"name\": \"John Doe\", "
                + "\"email\": \"john@example.com\", "
                + "\"login\": \"johndoe\", "
                + "\"birthday\": \"1990-01-01\"}";

        UserDto userDto = objectMapper.readValue(json, UserDto.class);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
        assertThat(userDto.getLogin()).isEqualTo("johndoe");
        assertThat(userDto.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void userDtoValidation_whenInvalidEmail_shouldReturnViolation() {
        UserDto userDto = new UserDto();
        userDto.setEmail("invalid-email");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Неверный формат электронной почты");
    }

    @Test
    void userDtoNameLogicTest() {
        UserDto userDto = new UserDto();

        userDto.setLogin("johndoe");
        userDto.setName(null);
        assertThat(userDto.getName()).isEqualTo("johndoe");

        userDto.setName("");
        assertThat(userDto.getName()).isEqualTo("johndoe");

        userDto.setName("John Doe");
        assertThat(userDto.getName()).isEqualTo("John Doe");
    }

    @Test
    void userPatchDtoSerializationTest() throws Exception {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setName("Updated Name");
        userPatchDto.setEmail("updated@example.com");
        userPatchDto.setLogin("updatedlogin");
        userPatchDto.setBirthday(LocalDate.of(1990, 1, 1));

        JsonContent<UserPatchDto> result = userPatchDtoJacksonTester.write(userPatchDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Updated Name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("updated@example.com");
        assertThat(result).extractingJsonPathStringValue("$.login").isEqualTo("updatedlogin");
        assertThat(result).extractingJsonPathStringValue("$.birthday").isEqualTo("1990-01-01");
    }

    @Test
    void userPatchDtoWithPartialDataTest() throws Exception {
        String json = "{\"name\": \"Only Name Updated\"}";

        UserPatchDto userPatchDto = objectMapper.readValue(json, UserPatchDto.class);

        assertThat(userPatchDto.getName()).isNotNull();
        assertThat(userPatchDto.getName()).isEqualTo("Only Name Updated");

        assertThat(userPatchDto.getEmail()).isNull();
        assertThat(userPatchDto.getLogin()).isNull();
        assertThat(userPatchDto.getBirthday()).isNull();
    }

    @Test
    void userPatchDtoDeserializationWithNullsTest() throws Exception {
        String json = "{}";

        UserPatchDto userPatchDto = objectMapper.readValue(json, UserPatchDto.class);

        assertThat(userPatchDto.getName()).isNull();
        assertThat(userPatchDto.getEmail()).isNull();
        assertThat(userPatchDto.getLogin()).isNull();
        assertThat(userPatchDto.getBirthday()).isNull();
    }
}