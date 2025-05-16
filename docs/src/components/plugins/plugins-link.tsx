import type React from "react";
import { GitHubTable } from "./github-table";
import { HangarTable } from "./hangar-table";
import { ModrinthTable } from "./modrinth-table";
import { SpigotTable } from "./spigot-table";

type Props = {
    pluginUrl: {
        spigot: string | null;
        hangar: string | null;
        modrinth: string | null;
        github: string | null;
    };
};

export const PluginLinks = ({ pluginUrl }: Props) => {
    let table: React.JSX.Element;

    if (pluginUrl.modrinth !== null) {
        table = <ModrinthTable url={pluginUrl.modrinth} />;
    } else if (pluginUrl.hangar !== null) {
        table = <HangarTable url={pluginUrl.hangar} />;
    } else if (pluginUrl.spigot !== null) {
        table = <SpigotTable url={pluginUrl.spigot} />;
    } else if (pluginUrl.github !== null) {
        table = <GitHubTable url={pluginUrl.github} />;
    }

    return (
        <>
            {table}

            <h2>リンク</h2>
            <ul>
                {pluginUrl.hangar !== null && (
                    <li>
                        <a href={pluginUrl.hangar}>Hangar</a>
                    </li>
                )}
                {pluginUrl.github !== null && (
                    <li>
                        <a href={pluginUrl.github}>GitHub</a>
                    </li>
                )}
                {pluginUrl.spigot !== null && (
                    <li>
                        <a href={pluginUrl.spigot}>Spigot</a>
                    </li>
                )}
                {pluginUrl.modrinth !== null && (
                    <li>
                        <a href={pluginUrl.modrinth}>Modrinth</a>
                    </li>
                )}
            </ul>
        </>
    );
};
