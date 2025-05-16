import { useEffect, useState } from "react";

type PluginData = {
    title: string;
    authors: string[];
    latestVersion: string;
    filesUrls: {
        filename: string;
        url: string;
    }[];
};

export const GitHubTable = ({ url }: { url: string }) => {
    const [plugins, setPlugins] = useState<PluginData>({
        title: "",
        authors: [],
        latestVersion: "",
        filesUrls: [],
    });

    const owner = url.split("/")[3];
    const repo = url.split("/")[4];
    const cacheKey = `github_${owner}_${repo}`;
    const cacheDuration = 36000; // 1 hour in milliseconds

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
                `https://api.github.com/repos/${owner}/${repo}`,
            );
            const data = await response.json();

            const authorResponse = await fetch(
                `https://api.github.com/repos/${owner}/${repo}/contributors`,
            );
            const authorData = (await authorResponse.json()) as {
                type: string;
                login: string;
            }[];

            const author = authorData
                .filter((it) => it.type === "User")
                .map((it) => it.login);

            const releasesResponse = await fetch(
                `https://api.github.com/repos/${owner}/${repo}/releases/latest`,
            );
            const releasesData = await releasesResponse.json();

            const filesUrls =
                releasesData?.assets !== null
                    ? releasesData.assets.map(
                          (release: { name: string; url: string }) => ({
                              filename: release.name,
                              url: release.url,
                          }),
                      )
                    : [];

            const pluginData: PluginData = {
                title: data.name,
                authors: author,
                latestVersion: releasesData?.tag_name || "N/A",
                filesUrls: filesUrls,
            };
            setPlugins(pluginData);
            localStorage.setItem(
                cacheKey,
                JSON.stringify({ data: pluginData, timestamp: Date.now() }),
            );
        };

        fetchPluginData().catch((error) => console.error(error));
    }, [owner, repo, cacheKey]);

    const length = 10;

    return (
        <table>
            <tbody>
                <tr>
                    <th>Name</th>
                    <td>{plugins.title}</td>
                </tr>
                <tr>
                    <th>Authors</th>
                    <td>
                        {plugins.authors.length > length
                            ? `${plugins.authors.slice(0, length).join(", ")} et al.`
                            : plugins.authors.join(", ")}
                    </td>
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
                                    className="flex-1 m-1 basis-full md:basis-1/3 lg:basis-1/4 whitespace-nowrap overflow-hidden text-ellipsis"
                                >
                                    <a
                                        key={file.filename}
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
