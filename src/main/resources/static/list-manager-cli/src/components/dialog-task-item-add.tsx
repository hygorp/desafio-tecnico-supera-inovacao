import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog";
import React, {useContext} from "react";
import {Button} from "@/components/ui/button";
import {cn} from "@/lib/utils";
import {DialogBody} from "next/dist/client/components/react-dev-overlay/internal/components/Dialog";
import {ErrorMessage, Field, Form, Formik} from "formik";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";
import {Label} from "@/components/ui/label";
import {Textarea} from "@/components/ui/textarea";
import {Input} from "@/components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {Check} from "lucide-react";
import {taskValidations} from "@/components/validations/validations";

const DialogTaskItemAdd = ({task} : {task: Task}) => {
    const {addItem} = useContext(AppContext);
    const {toast} = useToast();

    const [open, setOpen] = React.useState(false);

    const handleFormikSubmit = async (item: Item) => {
        await addItem(task.id, item).then((status) => {
            if (status === 200) {
                toast({
                    title: "Sucesso!",
                    description: `${item.title} foi adcionado à tarefa ${task.title}`
                })

                setOpen(false);
            }
        });
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant={"default"} size={"sm"} className={cn_button}>
                    Novo Item
                </Button>
            </DialogTrigger>

            <DialogContent className={"sm:max-w-[720px]"}>
                <DialogHeader>
                    <DialogTitle>
                        Novo Item
                    </DialogTitle>
                    <DialogDescription>
                        Incluir novo item a <strong>{task.title}</strong>
                    </DialogDescription>
                </DialogHeader>

                <DialogBody>
                    <Formik
                        initialValues={formInitialValues}
                        validationSchema={taskValidations}
                        onSubmit={async (values) => {
                            await handleFormikSubmit({
                                title: values.title.trim(),
                                description: values.description.trim(),
                                priority: values.priority,
                                state: values.state
                            } as Item);
                        }}
                    >
                        {({errors, touched, values, setFieldValue}) => (
                            <Form tabIndex={0} className={"flex flex-col space-y-7 outline-0"}>
                                <div className={"w-full flex flex-row items-center space-x-1.5s"}>
                                    <div className={"w-full flex flex-col space-y-2"}>
                                        <Label>Título</Label>
                                        <Field
                                            as={Input}
                                            name={"title"}
                                            type={"text"}
                                            className={`
                                            ${!errors.title || !touched.title ?
                                                ''
                                                :
                                                'border-red-500 dark:border-red-600 focus-visible:ring-2 focus-visible:ring-red-400'
                                            }
                                            `}
                                        />
                                        <ErrorMessage name={"title"} component={"div"}
                                                      className={"text-xs font-semibold text-red-500"}/>
                                    </div>
                                </div>

                                <div className={"w-full flex flex-row items-center space-x-1.5"}>
                                    <div className={"w-full flex flex-col space-y-2"}>
                                        <Label>Descrição</Label>
                                        <Field
                                            as={Textarea}
                                            name={"description"}
                                            type={"textarea"}
                                            className={`
                                            ${!errors.description || !touched.description ?
                                                ''
                                                :
                                                'border-red-500 dark:border-red-600 focus-visible:ring-2 focus-visible:ring-red-400'
                                            }
                                            `}
                                        />
                                        <ErrorMessage name={"description"} component={"div"}
                                                      className={"text-xs font-semibold text-red-500"}/>
                                    </div>
                                </div>

                                <div className={"w-full flex flex-row items-center space-x-2.5"}>
                                    <div className={"w-full flex flex-col space-y-2"}>
                                        <Label>Prioridade</Label>
                                        <Select
                                            name={"priority"}
                                            value={values.priority}
                                            onValueChange={(value) => {
                                                setFieldValue("priority", value).then()
                                            }}
                                        >
                                            <SelectTrigger
                                                className={`
                                            ${!errors.priority || !touched.priority ?
                                                    ''
                                                    :
                                                    'border-red-500 dark:border-red-600 focus-visible:ring-2 focus-visible:ring-red-400'
                                                }
                                            `}
                                            >
                                                <SelectValue placeholder={"Selecione a Prioridade"}/>
                                                <SelectContent>
                                                    <SelectItem value={"Baixa"}>Baixa</SelectItem>
                                                    <SelectItem value={"Media"}>Média</SelectItem>
                                                    <SelectItem value={"Alta"}>Alta</SelectItem>
                                                </SelectContent>
                                            </SelectTrigger>
                                        </Select>

                                        <ErrorMessage name={"priority"} component={"div"}
                                                      className={"text-xs text-red-500"}/>
                                    </div>

                                    <div className={"w-full flex flex-col space-y-2"}>
                                        <Label>Estado</Label>
                                        <Select
                                            name={"state"}
                                            value={values.state}
                                            onValueChange={(value) => {
                                                setFieldValue("state", value).then()
                                            }}
                                        >
                                            <SelectTrigger
                                                className={`
                                                ${!errors.state || !touched.state ?
                                                        ''
                                                        :
                                                        'border-red-500 dark:border-red-600 focus-visible:ring-2 focus-visible:ring-red-400'
                                                    }
                                                `}
                                            >
                                                <SelectValue placeholder={"Selecione o Estado"}/>
                                                <SelectContent>
                                                    <SelectItem value={"Pendente"}>Pendente</SelectItem>
                                                    <SelectItem value={"Fazendo"}>Fazendo</SelectItem>
                                                    <SelectItem value={"Completo"}>Completo</SelectItem>
                                                    <SelectItem value={"Cancelado"}>Cancelado</SelectItem>
                                                </SelectContent>
                                            </SelectTrigger>
                                        </Select>

                                        <ErrorMessage name={"state"} component={"div"}
                                                      className={"text-xs text-red-500"}/>
                                    </div>
                                </div>

                                <Button type={"submit"}>
                                    <Check className={"mr-2 w-4 h-4"}/>
                                    Salvar Item
                                </Button>
                            </Form>
                        )}
                    </Formik>
                </DialogBody>
            </DialogContent>
        </Dialog>
    )

}

export default DialogTaskItemAdd;

const cn_button = cn("border border-primary");

const formInitialValues = {title: "", description: "", priority: "", state: ""};
