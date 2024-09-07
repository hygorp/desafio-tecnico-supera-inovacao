import {cn} from "@/lib/utils";
import DialogTaskSave from "@/components/dialog-task-save";

const Navbar = () => {
    return (
        <nav className={cn(cn_navbar)}>
            <div>
                <h1 className={cn(cn_logo_font)}>
                    Tasks!
                </h1>
            </div>

            <div>
                <DialogTaskSave/>
            </div>
        </nav>
    )
}

export default Navbar;

const cn_navbar = cn("flex flex-row justify-between items-center px-20 py-4 bg-white border-b");
const cn_logo_font = cn("logo-font text-3xl text-primary");

