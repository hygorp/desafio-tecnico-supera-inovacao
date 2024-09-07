import * as Yup from "yup";

export const ItemValidations = Yup.object({
    title: Yup.string()
        .required("Título é obrigatório")
        .min(10, "O título deve ter no mínimo 10 caracteres"),

    description: Yup.string()
        .required("Descrição é obrigatório"),

    expiresAt: Yup.string()
        .required("Data de expiração é obrigatório")
        .test(
            'is-future-date',
            'A data e hora devem ser no futuro',
            (value) => {
                if (!value) {
                    return false;
                }

                const inputDate = new Date(value);

                const now = new Date();

                return inputDate > now;
            }
        ),
});

export const taskValidations = Yup.object({
        title: Yup.string()
            .required("Título é obrigatório")
            .min(10, "O título deve ter no mínimo 10 caracteres"),

        description: Yup.string()
            .required("Descrição é obrigatório"),

        priority: Yup.string()
            .required("Prioridade é obrigatório"),

        state: Yup.string()
            .required("Estado é obrigatório"),
    }
);