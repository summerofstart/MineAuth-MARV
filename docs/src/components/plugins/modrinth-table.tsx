import { useEffect, useState } from "react";

type PluginData = {
    title: string;
    authors: string[];
    license: string;
    latestVersion: string;
    filesUrls: {
        filename: string;
        url: string;
    }[];
};

export const ModrinthTable = ({ url }: { url: string }) => {
    const [plugins, setPlugins] = useState<PluginData>({
        title: "",
        authors: [],
        license: "",
        latestVersion: "",
        filesUrls: [],
    });

    const slug = url.split("/")[4];
    const cacheKey = `modrinth_${slug}`;
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
                `https://api.modrinth.com/v2/project/${slug}`,
            );
            const data = await response.json();
            const title = data.title;
            const license = data.license.name;
            const authorsResponse = await fetch(
                `https://api.modrinth.com/v2/project/${slug}/members`,
            );
            const authorsData = await authorsResponse.json();
            const authors: string[] = [
                ...authorsData
                    .filter((it: { role: string }) => it.role === "Owner")
                    .map(
                        (author: { user: { username: string } }) =>
                            author.user.username,
                    ),
                ...authorsData
                    .filter((it: { role: string }) => it.role !== "Owner")
                    .map(
                        (author: { user: { username: string } }) =>
                            author.user.username,
                    ),
            ];

            const fetchPluginVersionResponse = await fetch(
                `https://api.modrinth.com/v2/project/${slug}/version`,
            );

            const fetchPluginVersionData =
                await fetchPluginVersionResponse.json();
            const latestVersion = fetchPluginVersionData[0].version_number;

            const filesUrls: {
                fileName: string;
                url: string;
            }[] = fetchPluginVersionData[0].files.map(
                (file: { filename: string; url: string }) => {
                    return {
                        filename: file.filename,
                        url: file.url,
                    };
                },
            );

            const pluginData = {
                title,
                authors,
                license,
                latestVersion,
                filesUrls,
            };
            setPlugins(pluginData);
            localStorage.setItem(
                cacheKey,
                JSON.stringify({ data: pluginData, timestamp: Date.now() }),
            );
        };

        fetchPluginData().catch((error) => console.error(error));
    }, [slug, cacheKey]);

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
                    <th>License</th>
                    <td>{plugins.license}</td>
                </tr>
                <tr>
                    <th>Latest Version</th>
                    <td>{plugins.latestVersion}</td>
                </tr>
                <tr>
                    <th>Download</th>
                    <td>
                        <div className="flex flex-wrap">
                            {plugins.filesUrls?.map((file) => (
                                <div
                                    key={file.filename}
                                    className="flex-1 m-1"
                                    style={{
                                        flexBasis: "30%",
                                        whiteSpace: "nowrap",
                                        overflow: "hidden",
                                        textOverflow: "ellipsis",
                                    }}
                                >
                                    <a
                                        href={file.url}
                                        target="_blank"
                                        rel="noreferrer"
                                    >
                                        {file.filename}
                                    </a>
                                </div>
                            ))}
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    );
};
