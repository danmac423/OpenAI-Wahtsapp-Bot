GITHUB_TOKEN=$1

VERSION=$(curl -L \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        https://api.github.com/user/packages/maven/whatsappbroski.whatsappbroski/versions | \
        jq -r '.[] | select(.name | test("SNAPSHOT") | not) | .name' | sort -V | tail -n1)

curl "https://maven.pkg.github.com/Bilboaoa/whatsappbroski/whatsappbroski.whatsappbroski/${VERSION}/whatsappbroski-${VERSION}.jar" \
  -H "Authorization: Bearer $GITHUB_TOKEN" -L -o "app.jar"