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
import React, {useContext} from "react";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";
import {CircleX} from "lucide-react";
import {Button} from "@/components/ui/button";

const DialogTaskItemClearAll = ({task} : {task: Task}) => {
    const { clearItems } = useContext(AppContext);
    const { toast } = useToast();

    const [open, setOpen] = React.useState(false);

    const handleClearItems = async (taskId: string) => {
        await clearItems(taskId).then((status) => {
            if (status === 200) {
                toast({
                    title: "Sucesso",
                    description: `Todos itens de ${task.title} foram excluídos`
                })

                setOpen(false);
            }
        })
    }

    return (
        <AlertDialog open={open} onOpenChange={setOpen}>
            <AlertDialogTrigger className={"w-full"}>
                <div
                    className={"relative flex cursor-pointer hover:bg-red-500 hover:text-white select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors focus:bg-accent focus:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50"}>
                    <CircleX className={"mr-2 h-4 w-4"}/>
                    <span>Excluir Todos Itens</span>
                </div>
            </AlertDialogTrigger>

            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Tem certeza?</AlertDialogTitle>
                    <AlertDialogDescription>
                        A ação de deletar todos itens de <strong>{task.title}</strong> não pode ser desfeita.
                    </AlertDialogDescription>
                </AlertDialogHeader>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancelar</AlertDialogCancel>
                    <Button variant={"destructive"} onClick={() => handleClearItems(task.id)}>Continuar</Button>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}

export default DialogTaskItemClearAll;
