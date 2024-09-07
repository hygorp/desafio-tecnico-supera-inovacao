"use client"

import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import {cn} from "@/lib/utils";
import React, {useContext} from "react";
import {DialogBody} from "next/dist/client/components/react-dev-overlay/internal/components/Dialog";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";
import {ErrorMessage, Field, Form, Formik} from "formik";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Check} from "lucide-react";
import {ItemValidations} from "@/components/validations/validations";

const DialogTaskSave = () => {
    const { saveTask } = useContext(AppContext);
    const { toast } = useToast();

    const [open, setOpen] = React.useState(false);

    const handleFormikSubmit = async (task: Task) => {
        await saveTask(task).then((status) => {
            if (status === 201) {
                toast({
                    title: "Sucesso!",
                    description: `Tarefa criada!`
                })

                setOpen(false);
            }
        });
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant={"default"} size={"sm"} className={cn_button}>
                    Nova Tarefa
                </Button>
            </DialogTrigger>

            <DialogContent className={"sm:max-w-[720px]"}>
                <DialogHeader>
                    <DialogTitle>
                        Nova Tarefa
                    </DialogTitle>
                    <DialogDescription>
                        Incluir nova tarefa
                    </DialogDescription>
                </DialogHeader>

                <DialogBody>
                    <Formik
                        initialValues={formInitialValues}
                        validationSchema={ItemValidations}
                        onSubmit={async (values) => {
                            await handleFormikSubmit({
                                title: values.title.trim(),
                                description: values.description.trim(),
                                expiresAt: values.expiresAt
                            } as Task);
                        }}
                    >
                        {({ errors, touched }) => (
                            <Form tabIndex={0} className={"flex flex-col space-y-7 outline-0"}>
                                <div className={"w-full flex flex-row justify-center items-center space-x-2.5"}>
                                    <div className={"w-full flex flex-col space-y-1.5"}>
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

                                    <div className={"w-4/6 flex flex-col space-y-1.5"}>
                                        <Label>Data de Expiração</Label>
                                        <Field
                                            as={Input}
                                            name={"expiresAt"}
                                            type={"date"}
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

export default DialogTaskSave;

const cn_button = cn("border border-primary");

const formInitialValues = {title: "", description: "", expiresAt: ""} as unknown as Task