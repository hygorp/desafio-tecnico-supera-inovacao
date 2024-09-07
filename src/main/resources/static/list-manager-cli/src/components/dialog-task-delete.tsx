import {
    AlertDialog,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog";
import {Trash2} from "lucide-react";
import {Button} from "@/components/ui/button";
import React, {useContext} from "react";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";

const DialogTaskDelete = ({task} : {task: Task}) => {
    const { deleteTask } = useContext(AppContext);
    const { toast } = useToast();

    const [open, setOpen] = React.useState(false);

    const handleTaskDelete = async (taskId: string) => {
        await deleteTask(taskId).then((status) => {
            if (status === 204) {
                toast({
                    title: "Sucesso!",
                    description: `${task.title} foi excluída.`
                })

                setOpen(false);
            }
        });
    }

    return (
        <AlertDialog open={open} onOpenChange={setOpen}>
            <AlertDialogTrigger asChild>
                <div className={"relative flex cursor-pointer hover:bg-red-500 hover:text-white select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors focus:bg-accent focus:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50"}>
                    <Trash2 className={"mr-2 h-4 w-4"}/>
                    <span>Excluir Tarefa</span>
                </div>
            </AlertDialogTrigger>

            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Tem certeza?</AlertDialogTitle>
                    <AlertDialogDescription>
                        A ação de deletar a tarefa <strong>{task.title}</strong> não pode ser desfeita, todos os itens também serão perdidos.
                    </AlertDialogDescription>
                </AlertDialogHeader>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancelar</AlertDialogCancel>
                    <Button variant={"destructive"} onClick={() => handleTaskDelete(task.id)}>Continuar</Button>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}

export default DialogTaskDelete;