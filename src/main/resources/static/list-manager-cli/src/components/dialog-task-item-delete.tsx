import {
    AlertDialog, AlertDialogCancel, AlertDialogContent,
    AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog";
import {Trash2} from "lucide-react";
import {Button} from "@/components/ui/button";
import React, {useContext} from "react";
import {AppContext} from "@/context/app-context";
import {useToast} from "@/hooks/use-toast";

const DialogTaskItemDelete = ({taskId, item} : {taskId: string, item: Item}) => {
    const { deleteItem } = useContext(AppContext);
    const { toast } = useToast();

    const [open, setOpen] = React.useState(false);

    const handleItemDelete = async (taskId: string, item: Item) => {
        await deleteItem(taskId, item).then((status) => {
            if (status === 200) {
                toast({
                    title: "Sucesso!",
                    description: `Seu item ${item.title} foi removido`
                })

                setOpen(false);
            }
        })
    }

    return (
        <AlertDialog open={open} onOpenChange={setOpen}>
            <AlertDialogTrigger className={"w-full"}>
                <div
                    className={"relative flex cursor-pointer hover:bg-gray-100 select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors focus:bg-accent focus:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50"}>
                    <Trash2 className={"mr-2 h-4 w-4"}/>
                    <span>Excluir</span>
                </div>
            </AlertDialogTrigger>

            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Tem certeza?</AlertDialogTitle>
                    <AlertDialogDescription>
                        A ação de deletar o item <strong>{item.title}</strong> não pode ser desfeita.
                    </AlertDialogDescription>
                </AlertDialogHeader>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancelar</AlertDialogCancel>
                    <Button variant={"destructive"} onClick={() => handleItemDelete(taskId, item)}>Continuar</Button>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}

export default DialogTaskItemDelete;
