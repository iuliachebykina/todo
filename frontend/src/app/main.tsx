import Overlay from "./overlay";
import TodoList from "./List/List";
import Header from "./header/header";
import { useReducer, useState } from "react";
import Settings from "./Settings";
import Donate from "./Donate";

const Main = () => {
    const [, forceUpdate] = useReducer(x => x + 1, 0);
    const [settingsVisible, setSettingsVisible] = useState(false);
    const [donateVisible, setDonateVisible] = useState(false);

    return (
        <div>
            <Donate shown={donateVisible} setShown={setDonateVisible} update={forceUpdate}/>
            <Settings shown={settingsVisible} setShown={setSettingsVisible} update={forceUpdate}/>
            <Overlay update={forceUpdate} setSettingsVisible={setSettingsVisible}/>
            <Header setShowSettings={setSettingsVisible} setShowDonate={setDonateVisible}/>
            <TodoList/>
        </div>
    )
}

export default Main