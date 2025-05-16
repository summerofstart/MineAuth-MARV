import { useEffect, useState } from "react";

type PluginData = {
    title: string;
    authors: string[];
    latestVersion: string;
    filesUrls: {
        filename: string;
        url: string;
    };
};

export const HangarTable = ({ url }: { url: string }) => {
    const [plugins, setPlugins] = useState<PluginData>({
        title: "",
        authors: [],
        latestVersion: "",
        filesUrls: {
            filename: "",
            url: "",
        },
    });

    const resId = url.split("/")[4].split(".")[1];
    const cacheKey = `spigot_${resId}`;
    const cacheDuration = 3600000; // 1 hour in milliseconds

    useEffect(() => {
        const fetchPluginData = async () => {
            const cachedData = localStorage.getItem(cacheKey);
            if (cachedData) {
                const { data, timestamp } = JSON.parse(cachedData);
                setPlugins(data);
                if (Date.now() - timestamp < cacheDuration) {
                    return;
                }
            }

            const response = await fetch(
                `https://api.spiget.org/v2/resources/${resId}`,
            );
            const data = await response.json();

            const authorId = data.author.id;

            const authorResponse = await fetch(
                `https://api.spiget.org/v2/authors/${authorId}`,
            );

            const authorData = await authorResponse.json();
            const versionId = data.version.id;

            const versionResponse = await fetch(
                `https://api.spiget.org/v2/resources/${resId}/versions/${versionId}`,
            );

            const versionData = await versionResponse.json();

            const pluginData: PluginData = {
                title: data.name,
                authors: [authorData.name],
                latestVersion: versionData.name,
                filesUrls: {
                    filename: versionData.name,
                    url: `https://api.spiget.org/v2/resources/${resId}/versions/${versionId}/download`,
                },
            };
            setPlugins(pluginData);
            localStorage.setItem(
                cacheKey,
                JSON.stringify({ data: pluginData, timestamp: Date.now() }),
            );
        };

        fetchPluginData().catch((error) => console.error(error));
    }, [resId, cacheKey]);

    return (
        <table>
            <tbody>
                <tr>
                    <th>Name</th>
                    <td>{plugins.title}</td>
                </tr>
                <tr>
                    <th>Authors</th>
                    <td>{plugins.authors.join(", ")}</td>
                </tr>
                <tr>
                    <th>Latest Version</th>
                    <td>{plugins.latestVersion}</td>
                </tr>
                <tr>
                    <th>Download</th>
                    <td>
                        <a
                            href={plugins.filesUrls.url}
                            target="_blank"
                            rel="noreferrer"
                        >
                            クリックしてダウンロード
                        </a>
                    </td>
                </tr>
            </tbody>
        </table>
    );
};
