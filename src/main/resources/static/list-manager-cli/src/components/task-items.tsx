import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuLabel,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import {Button} from "@/components/ui/button";
import {EllipsisVertical, ListFilter} from "lucide-react";
import DialogTaskItemUpdate from "@/components/dialog-task-item-update";
import DialogTaskItemDelete from "@/components/dialog-task-item-delete";
import {CardContent} from "@/components/ui/card";
import React, {useEffect} from "react";
import {Badge} from "@/components/ui/badge";

const TaskItems = ({taskId, items}: { taskId: string, items: Item[] }) => {
    const priorityMap: Record<string, number> = {"Baixa": 1, "Media": 2, "Alta": 3};
    const stateMap: Record<string, number> = {"Pendente": 1, "Fazendo": 2, "Completo": 3, "Cancelado": 4};

    const [sortedItems, setSortedItems] = React.useState<Item[]>([]);
    const [actFilter, setActFilter] = React.useState(0);
    
    useEffect(() => {
        setSortedItems(items);
    }, [items])

    const sortItemsByPriority = () => {
        const sorted = [...items].sort((a, b) => priorityMap[b.priority] - priorityMap[a.priority]);
        setSortedItems(sorted);
    };

    const sortItemsByState = () => {
        const sorted = [...items].sort((a, b) => stateMap[a.state] - stateMap[b.state]);
        setSortedItems(sorted);
    };

    const clearFilter = () => {
        setSortedItems(items);
        setActFilter(0);
    }

    const classifyPriorityBadge = (priority: string) => {
        switch (priority) {
            case "Baixa":
                return <Badge variant={"low"}>Prioridade: {priority}</Badge>
            case "Media":
                return <Badge variant={"warning"}>Prioridade: {priority}</Badge>
            case "Alta":
                return <Badge variant={"destructive"}>Prioridade: {priority}</Badge>
            default:
                return "";
        }
    }

    const classifyStateBadge = (state: string) => {
        switch (state) {
            case "Pendente":
                return <Badge variant={"pending"}>Status: {state}</Badge>
            case "Fazendo":
                return <Badge variant={"doing"}>Status: {state}</Badge>
            case "Completo":
                return <Badge variant={"completed"}>Status: {state}</Badge>
            case "Cancelado":
                return <Badge variant={"canceled"}>Status: {state}</Badge>
            default:
                return "";
        }
    }

    return (
        <>
            <div className={"flex flex-col pl-6 pr-10 pb-5 space-y-1.5"}>
                <h4 className={"text-sm font-medium"}>Filtrar Itens</h4>

                <div className={"flex flex-row justify-between"}>
                    <div>
                        <div className={"flex flex-row gap-2"}>
                            <Button variant={actFilter === 1 ? "secondary" : "outline"} size={"sm"} onClick={() => {
                                setActFilter(1)
                                sortItemsByPriority()
                            }}>
                                <ListFilter className={"h-4 w-4 mr-2"}/>
                                Por Prioridade
                            </Button>

                            <Button variant={actFilter === 2 ? "secondary" : "outline"} size={"sm"} onClick={() => {
                                setActFilter(2)
                                sortItemsByState()
                            }}>
                                <ListFilter className={"h-4 w-4 mr-2"}/>
                                Por Status
                            </Button>
                        </div>
                    </div>

                    <div>
                        <Button variant={"outline"} size={"sm"} onClick={clearFilter}>
                            Limpar Filtros
                        </Button>
                    </div>
                </div>

            </div>

            <CardContent className={"max-h-[380px] h-[380px] space-y-1.5 overflow-y-scroll"}>
                {sortedItems
                    .map((item: Item, index: number) => (
                        <div className="flex items-start space-x-4 rounded-md border h-32 p-4"
                             key={index}>
                            <div className="flex-1 space-y-1">
                                <p className="text-sm font-medium leading-none">
                                    {item.title}
                                </p>
                                <p className="text-sm text-muted-foreground">
                                {item.description}
                                    </p>

                                    <div className={"flex flex-row space-x-5 pt-5"}>
                                        <span>{classifyPriorityBadge(item.priority)}</span>
                                        <span>{classifyStateBadge(item.state)}</span>
                                    </div>
                                </div>

                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <Button variant={"ghost"} size={"icon"}>
                                            <EllipsisVertical className={"w-4 h-4"}/>
                                        </Button>
                                    </DropdownMenuTrigger>

                                    <DropdownMenuContent align={"end"} className={"w-44"}>
                                        <DropdownMenuLabel>Ações do Item</DropdownMenuLabel>
                                        <DropdownMenuGroup>
                                            <DialogTaskItemUpdate taskId={taskId} item={item}/>
                                            <DialogTaskItemDelete taskId={taskId} item={item}/>
                                        </DropdownMenuGroup>
                                    </DropdownMenuContent>

                                </DropdownMenu>
                            </div>
                        ))}
                </CardContent>
            </>
            )
            }

            export default TaskItems;
