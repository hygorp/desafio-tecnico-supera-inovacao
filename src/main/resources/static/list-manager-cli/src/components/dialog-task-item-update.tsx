import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import {Check, Pencil} from "lucide-react";
import React, {useContext} from "react";
import {DialogBody} from "next/dist/client/components/react-dev-overlay/internal/components/Dialog";
import {ErrorMessage, Field, Form, Formik} from "formik";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";
import {taskValidations} from "@/components/validations/validations";

const DialogTaskItemUpdate = ({taskId, item} : {taskId: string, item: Item}) => {
    const { updateItem } = useContext(AppContext);
    const { toast } = useToast();

    const [open, setOpen] = React.useState(false);

    const handleFormikSubmit = async (item: Item) => {
        await updateItem(taskId, item).then((status) => {
            if (status === 200) {
                toast({
                    title: "Sucesso!",
                    description: `Item ${item.title} foi atualizado`,
                })

                setOpen(false);
            }
        });
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <div
                    className={"relative flex cursor-pointer hover:bg-gray-100 select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors focus:bg-accent focus:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50"}>
                    <Pencil className={"mr-2 h-4 w-4"}/>
                    <span>Editar</span>
                </div>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>
                        Editando - {item.title}
                    </DialogTitle>
                    <DialogDescription>
                        Editar um item da sua tarefa
                    </DialogDescription>
                </DialogHeader>

                <DialogBody>
                <Formik
                        initialValues={{id: item.id, title: item.title, description: item.description, priority: item.priority, state: item.state}}
                        validationSchema={taskValidations}
                        onSubmit={async (values) => {
                            await handleFormikSubmit({
                                id: values.id,
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
                                            value={values.title}
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
                                            value={values.description}
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
                                                <SelectValue placeholder={values.priority}/>
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
                                                <SelectValue placeholder={values.state}/>
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

export default DialogTaskItemUpdate;
