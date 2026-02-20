package br.com.doeaqui.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    String name,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    String email,

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    String phone,

    @Size(min = 6, message = "A senha precisa ter no minímo 6 caracteres")
    String password
) {}
