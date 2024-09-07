"use client"

import Navbar from "@/components/navbar";
import React, {useContext, useEffect} from "react";
import {AppContext} from "@/context/app-context";
import {Card, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card";
import DialogTaskItemAdd from "@/components/dialog-task-item-add";
import {Button} from "@/components/ui/button";
import {EllipsisVertical, ListFilter} from "lucide-react";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import DialogTaskDelete from "@/components/dialog-task-delete";
import DialogTaskUpdate from "@/components/dialog-task-update";
import DialogTaskItemClearAll from "@/components/dialog-task-item-clear-all";
import TaskItems from "@/components/task-items";
import {Badge} from "@/components/ui/badge";
import {Separator} from "@/components/ui/separator";
import {Input} from "@/components/ui/input";

export default function Home() {
    const {tasks} = useContext(AppContext);

    const [allTasks, setAllTasks] = React.useState<Task[]>([])
    const [searchText, setSearchText] = React.useState("");

    const [startDate, setStartDate] = React.useState<string>("")
    const [endDate, setEndDate] = React.useState<string>("")

    const [actFilter, setActFilter] = React.useState(0);

    useEffect(() => {
        setAllTasks(Array.from(tasks));
    }, [tasks]);

    const sortByDateASC = () => {
        const sorted = [...Array.from(tasks)].sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
        setAllTasks(sorted);
    }

    const sortByDateDESC = () => {
        const sorted = [...Array.from(tasks)].sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        setAllTasks(sorted);
    }

    const filterByDateInterval = () => {
        const normalizeDate = (date: Date) => {
            return new Date(date.getFullYear(), date.getMonth(), date.getDate());
        }

        const normalizedStartDate = normalizeDate(new Date(startDate));
        const normalizedEndDate = normalizeDate(new Date(endDate));

        const filteredTasks = [...Array.from(tasks)].filter((task) => {
            const taskDate = normalizeDate(new Date(task.createdAt));
            return taskDate >= normalizedStartDate && taskDate <= normalizedEndDate;
        });

        setAllTasks(filteredTasks);
    }

    const clearFilter = () => {
        setAllTasks(Array.from(tasks));
        setStartDate("");
        setEndDate("");
        setActFilter(0);
        setSearchText("");
    }

    return (
        <main>
            <Navbar/>
            <div className={"flex flex-row justify-start items-center gap-5 px-20 pt-5"}>
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant={"default"} size={"default"}>
                            <ListFilter className={"mr-2"}/>
                            Filtrar Tarefas
                        </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent className={"w-[400px]"} align={"start"}>
                        <DropdownMenuLabel>Filtros</DropdownMenuLabel>
                        <DropdownMenuSeparator/>
                        <DropdownMenuGroup>
                            <Button
                                className={"w-full justify-start"}
                                variant={actFilter === 1 ? "secondary" : "ghost"}
                                size={"sm"}
                                onClick={() => {
                                    setActFilter(1)
                                    sortByDateASC()
                                }}
                            >
                                Mais Antigas
                            </Button>

                            <Button
                                className={"w-full justify-start"}
                                variant={actFilter === 2 ? "secondary" : "ghost"}
                                size={"sm"}
                                onClick={() => {
                                    setActFilter(2)
                                    sortByDateDESC()
                                }}
                            >
                                Mais Recentes
                            </Button>
                        </DropdownMenuGroup>
                        <DropdownMenuSeparator/>
                        <DropdownMenuGroup>
                            <div className={"flex flex-col px-3 py-2 space-y-1.5"}>
                                <p className={"text-sm font-semibold"}>Por Intervalo</p>
                                <div className={"flex flex-row items-center gap-2"}>
                                    <Input
                                        placeholder={"Inicio"}
                                        type={"date"}
                                        value={startDate}
                                        onChange={(e) => setStartDate(e.target.value)}
                                    />

                                    <Input
                                        placeholder={"Fim"}
                                        type={"date"}
                                        value={endDate}
                                        onChange={(e) => setEndDate(e.target.value)}
                                    />

                                    <Button
                                        className={"justify-start"}
                                        variant={"default"}
                                        size={"sm"}
                                        onClick={filterByDateInterval}
                                        disabled={!(startDate && endDate)}
                                    >
                                        Filtrar
                                    </Button>
                                </div>
                            </div>
                        </DropdownMenuGroup>
                    </DropdownMenuContent>
                </DropdownMenu>

                <Input
                    className={"w-2/6"}
                    placeholder={"Buscar por título"}
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                />

                <Button variant={"outline"} size={"default"} onClick={clearFilter}>Limpar Filtros</Button>
            </div>

            <div className={"flex flex-row  flex-wrap gap-5 px-20 py-10"}>
                {allTasks
                    .filter((task) => task.title.toLowerCase().includes(searchText))
                    .map((task: Task, index: number) => (
                    <Card key={index} className={"w-[550px]"}>
                        <CardHeader className={"max-h-40 h-40"}>
                            <CardTitle>{task.title} </CardTitle>
                            <CardDescription>{task.description}</CardDescription>
                            <Separator/>
                            <div className={"flex flex-row justify-between"}>
                                <div className={"flex flex-col"}>
                                    <Badge>Criado em {new Date(task.createdAt).toLocaleDateString("pt-BR", {
                                        day: '2-digit',
                                        month: '2-digit',
                                        year: 'numeric',
                                        timeZone: 'UTC'
                                    })}</Badge>
                                </div>

                                <div className={"flex flex-col"}>
                                    <Badge>Atualizado em {new Date(task.updatedAt).toLocaleDateString("pt-BR", {
                                        day: '2-digit',
                                        month: '2-digit',
                                        year: 'numeric',
                                        timeZone: 'UTC'
                                    })}</Badge>
                                </div>

                                <div className={"flex flex-col"}>
                                    <Badge>Expira em {new Date(task.expiresAt).toLocaleDateString("pt-BR", {
                                        day: '2-digit',
                                        month: '2-digit',
                                        year: 'numeric',
                                        timeZone: 'UTC'
                                    })}</Badge>
                                </div>
                            </div>
                            <Separator/>
                        </CardHeader>

                        <TaskItems taskId={task.id} items={task.items}/>

                        <CardFooter className={"flex flex-col space-y-2.5 mt-10"}>
                            <div className={"w-full flex flex-row justify-between items-center"}>
                                <DialogTaskItemAdd task={task}/>

                                <div>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger asChild>
                                            <Button variant={"secondary"} size={"sm"}>
                                                <EllipsisVertical className={"w-4 h-4"}/>
                                            </Button>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent align={"end"} className={"w-44"}>
                                            <DropdownMenuLabel>Ações da Tarefa</DropdownMenuLabel>
                                            <DropdownMenuGroup>
                                                <DialogTaskUpdate task={task}/>
                                                <DialogTaskDelete task={task}/>
                                                <DropdownMenuSeparator/>
                                                <DialogTaskItemClearAll task={task}/>
                                            </DropdownMenuGroup>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </div>
                            </div>
                        </CardFooter>
                    </Card>
                ))}
            </div>
        </main>
    );
}
