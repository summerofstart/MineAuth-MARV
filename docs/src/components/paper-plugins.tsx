import { useEffect, useState } from "react";
import { Plugin } from "./plugin";

type server = "lobby" | "main" | "res";

type PluginType = {
    [key in server]: string;
};

export const PaperPlugins = ({ server }: { server: server }) => {
    const [plugins, setPlugins] = useState<string[]>([]);

    useEffect(() => {
        const fetchPlugins = async () => {
            const response = await fetch(
                `https://api.morino.party/${server}/api/v1/commons/server/plugins`,
            );
            const data = (await response.json()) as string[];
            data.sort();
            setPlugins(data);
        };

        fetchPlugins();
    }, [server]);

    return (
        <ul>
            {plugins.map((plugin) => (
                <li key={plugin}>
                    <Plugin plugin={plugin} />
                </li>
            ))}
        </ul>
    );
};
