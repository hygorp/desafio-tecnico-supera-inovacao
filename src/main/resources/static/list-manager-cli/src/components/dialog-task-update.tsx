import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog";
import {Check, Pencil} from "lucide-react";
import React, {useContext} from "react";
import {ErrorMessage, Field, Form, Formik} from "formik";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Button} from "@/components/ui/button";
import {DialogBody} from "next/dist/client/components/react-dev-overlay/internal/components/Dialog";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";
import {ItemValidations} from "@/components/validations/validations";

const DialogTaskUpdate = ({task} : {task: Task}) => {
    const { updateTask } = useContext(AppContext);

    const { toast } = useToast();

    const [open, setOpen] = React.useState(false);

    const handleFormikSubmit = async (taskId: string, task: Task) => {
        await updateTask(taskId, task).then((status) => {
            if (status === 200) {
                toast({
                    title: "Sucesso!",
                    description: `${task.title} foi atualizada`
                })

                setOpen(false);
            }

            if (status === 409) {
                toast({
                    variant: "destructive",
                    title: "Erro!",
                    description: "Data inválida"
                })
            }
        });
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <div
                    className={"relative flex cursor-pointer hover:bg-gray-100 select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors focus:bg-accent focus:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50"}>
                    <Pencil className={"mr-2 h-4 w-4"}/>
                    <span>Editar Tarefa</span>
                </div>
            </DialogTrigger>

            <DialogContent className={"sm:max-w-[720px]"}>
                <DialogHeader>
                    <DialogTitle>
                        Editando - {task.title}
                    </DialogTitle>
                    <DialogDescription>
                        Editar sua tarefa
                    </DialogDescription>
                </DialogHeader>

                <DialogBody>
                    <Formik
                        initialValues={{title: task.title, description: task.description, expiresAt: task.expiresAt}}
                        validationSchema={ItemValidations}
                        onSubmit={async (values) => {
                            await handleFormikSubmit(task.id, {title: values.title.trim(), description: values.description.trim(), expiresAt: values.expiresAt} as Task);
                        }}
                    >
                        {({ errors, touched, values, setFieldValue }) => (
                            <Form tabIndex={0} className={"flex flex-col space-y-7 outline-0"}>
                                <div className={"w-full flex flex-row justify-center items-center space-x-2.5"}>
                                    <div className={"w-full flex flex-col space-y-1.5"}>
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

                                    <div className={"w-4/6 flex flex-col space-y-1.5"}>
                                        <Label>Data de Expiração</Label>
                                        <Field
                                            as={Input}
                                            name={"expiresAt"}
                                            type={"date"}
                                            value={values.expiresAt}
                                            onChange={(e: { target: { value: never; }; }) => {
                                                console.log("Novo valor:", e.target.value);
                                                setFieldValue("expiresAt", e.target.value).then();
                                            }}
                                            className={`
                                            ${!errors.expiresAt || !touched.expiresAt ?
                                                    ''
                                                    :
                                                    'border-red-500 dark:border-red-600 focus-visible:ring-2 focus-visible:ring-red-400'
                                                }
                                            `}
                                        />
                                        <ErrorMessage name={"expiresAt"} component={"div"}
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
                                            values={values.description}
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

                                <div>
                                    <Button type={"submit"}>
                                        <Check className={"mr-2 w-4 h-4"}/>
                                        Salvar Tarefa
                                    </Button>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </DialogBody>
            </DialogContent>
        </Dialog>
    )
}

export default DialogTaskUpdate;
